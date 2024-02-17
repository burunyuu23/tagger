package ru.dnlkk.tagger.infrastructure.front_controller

import api.longpoll.bots.model.objects.basic.Message
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import ru.dnlkk.tagger.dto.repository_dto.HelpDocDto
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.haveNeedRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.*
import ru.dnlkk.tagger.repository.UserRepository
import java.lang.reflect.ParameterizedType
import kotlin.jvm.optionals.getOrNull
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.javaType
import kotlin.reflect.jvm.jvmName

@Component
class TaggerFrontController {
    @Autowired
    private lateinit var context: ApplicationContext

    @Autowired
    private lateinit var userRepository: UserRepository

    private val controllers: MutableMap<String, TaggerController<Any>> = HashMap()
    private val documentation: MutableMap<HelpDocDto, MutableMap<HelpDocDto, String>> = HashMap()

    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostConstruct
    private fun init() {
        val beans = this.context.getBeansWithAnnotation(TaggerMapping::class.java)
        controllers.putAll(
            beans.toList().associateNotNull { (_, bean) ->
                val clazz: KClass<*> = bean::class
                val annotation = clazz.findAnnotation<TaggerMapping>()
                val key = annotation?.value.takeIf { !it.isNullOrBlank() && it !in controllers }
                    ?: clazz.jvmName
                key to (bean as TaggerController<Any>)
            }
        )

        initDocumentation()
        injectDocumentation()
    }

    fun initDocumentation() {
        controllers.forEach { (key, controller) ->

            val taggerControllerNeedRole = controller::class.findAnnotation<TaggerMappingRole>()?.value
            controller::class.memberFunctions
                .filter { it.findAnnotation<TaggerDocumented>() != null }
                .forEach { method ->
                    val taggerDocumentedAnnotation = method.findAnnotation<TaggerDocumented>()
                    val taggerMethodNeedRole = method.findAnnotation<TaggerMappingRole>()?.value
                    val subArgsKey = method.findAnnotation<TaggerArgsMapping>()?.value ?: "MAIN"
                    val description =
                        taggerDocumentedAnnotation!!.description +
                                if (taggerDocumentedAnnotation.example.isNotBlank())
                                    "\nПример: ${taggerDocumentedAnnotation.example}"
                                else ""

                    documentation.computeIfAbsent(HelpDocDto(key, taggerControllerNeedRole?.toList()))
                    { HashMap() }[HelpDocDto(subArgsKey, taggerMethodNeedRole?.toList())] = description
                }
        }
    }

    private fun injectDocumentation() {
        val taggerHelp =
            controllers.filter { it.value::class.hasAnnotation<TaggerHelp>() }.values.firstOrNull() ?: return
        try {
            val documentationField = taggerHelp.javaClass.getDeclaredField("documentation")
            documentationField.isAccessible = true
            documentationField[taggerHelp] = documentation
        } catch (e: NoSuchFieldException) {
            log.error(e.message)
        }
    }

    fun dispatch(message: Message): MessageBuilder? {
        val subArgs = argSplit(message.text)

        val messageBuilder = MessageBuilder.Builder(message.peerId)

        val user = userRepository.findById(message.fromId).getOrNull()
        try {
            for ((key, controller) in controllers) {
                if (controller.mapping(message.text, key)) {
                    val needRoles = controller::class.findAnnotation<TaggerMappingRole>()?.value
                    if (!haveNeedRole(needRoles?.toList(), user)) {
                        throw Exception("Тебе сюда нельзя")
                    }

                    val dto = createDtoInstance(controller)

                    for ((subArgsKey, args) in subArgs.entries) {
                        val controllerMethod = controller::class.memberFunctions.find {
                            subArgsKey == it.findAnnotation<TaggerArgsMapping>()?.value
                        }
                        if (controllerMethod == null) {
                            throw Exception(
                                "Ошибка при вызове метода " +
                                        (controllers["/h"]
                                            ?.javaClass
                                            ?.getMethod("buildDocBlock", Map.Entry::class.java)
                                            ?.invoke(controllers["/h"], documentation.entries.find { it.key.path == key })
                                            ?: "").toString()
                            )
                        }
                        handleMethodInvocation(
                            controllerMethod,
                            controller,
                            message,
                            user,
                            args,
                            messageBuilder,
                            dto
                        )
                    }

                    return controller.handle(message, user, subArgs, messageBuilder, dto)

                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(e.message)
            messageBuilder.clearMessage(e.message)
            return messageBuilder.build()
        }

        return null
    }

    private fun createDtoInstance(controller: TaggerController<Any>): Any {
        val genericType =
            (controller.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>
        return genericType.getDeclaredConstructor().newInstance()
    }

    private fun handleMethodInvocation(
        method: KFunction<*>,
        controller: TaggerController<Any>,
        message: Message,
        user: User?,
        subArgs: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: Any
    ) {
        try {
            val methodArgs = method.parameters[3].type.javaType as Class<*>
            if (methodArgs == Boolean::class.java) {
                method.call(controller, message, true, messageBuilder, dto)
                return
            }
            val isArray = methodArgs.isArray
            var checkMethodArgs = methodArgs
            if (isArray) {
                checkMethodArgs = methodArgs.componentType
            }
            val convertedArgs = subArgs.map { param ->
                var convertedParam = when (checkMethodArgs) {
                    Number::class.javaObjectType, Number::class.javaPrimitiveType -> param.toInt()
                    Int::class.javaObjectType, Int::class.javaPrimitiveType -> param.toInt()
                    Boolean::class.javaObjectType, Boolean::class.javaPrimitiveType -> param.toBoolean()
                    Long::class.javaObjectType, Long::class.javaPrimitiveType -> param.toLong()
                    Double::class.javaObjectType, Double::class.javaPrimitiveType -> param.toDouble()
                    else -> param
                }
                if (checkMethodArgs.isEnum) {
                    convertedParam = checkMethodArgs.getDeclaredMethod("valueOf", String::class.java)
                        .invoke(methodArgs, param)
                }
                checkMethodArgs.cast(convertedParam)
            }
            val resultArray = java.lang.reflect.Array.newInstance(checkMethodArgs, convertedArgs.size) as Array<*>
            for (i in 0 until convertedArgs.size) {
                resultArray[i] = convertedArgs[i] as Nothing
            }
            var callArg: Any? = resultArray
            if (!isArray) {
                callArg = resultArray[0]
            }
            method.call(controller, message, user, callArg, messageBuilder, dto)
        } catch (e: Exception) {
            val adminInfo = "MethodName: ${method.name}, args: ${subArgs.toList()}, "
            val argMapping = method.findAnnotation<TaggerArgsMapping>()?.value
            val controllerPath = controller::class.findAnnotation<TaggerMapping>()?.value
            val mappingDoc = documentation[HelpDocDto(controllerPath ?: "")]?.get(HelpDocDto(argMapping ?: ""))
            val errorInfo = "Error: ${e.message}" +
                    if (mappingDoc != null) "\n\nМесто ошибки:\n$argMapping: $mappingDoc" else ""
            log.error(adminInfo + errorInfo)
            throw Exception(errorInfo)
        }
    }

    fun argSplit(input: String): Map<String, Array<String>> {
        val pattern = "(-\\S+)(?:\\s(?!-\\S+)(.*?)(?=\\s-\\S+|\$))?".toRegex()
        val matches = pattern.findAll(input)
        val result = LinkedHashMap<String, Array<String>>()

        for (match in matches) {
            val key = match.groupValues[1]
            val value: Array<String> = if (match.groups.size > 1)
                match.groupValues[2].trim().split(" ").toTypedArray()
            else
                arrayOf()

            result[key] = value
        }
        return result
    }
}

private inline fun <K, V, R : Any> Iterable<Pair<K, V>>.associateNotNull(transform: (Pair<K, V>) -> Pair<K, R>?): Map<K, R> {
    val destination = LinkedHashMap<K, R>()
    for (element in this) {
        val (key, value) = transform(element) ?: continue
        destination[key] = value
    }
    return destination
}
