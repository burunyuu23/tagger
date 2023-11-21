package ru.dnlkk.tagger.infrastructure.front_controller

import api.longpoll.bots.model.objects.basic.Message
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerDocumented
import ru.dnlkk.tagger.infrastructure.annotation.TaggerHelp
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.jvm.jvmName

@Component
class TaggerFrontController {
    @Autowired
    private lateinit var context: ApplicationContext
    private val controllers: MutableMap<String, TaggerController<Any>> = HashMap()
    private val documentation: MutableMap<String, MutableMap<String, String>> = HashMap()

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

        println(controllers)

        initDocumentation()
        injectDocumentation()
    }

    fun initDocumentation() {
        controllers.forEach { (key, controller) ->
            controller::class.memberFunctions
                .filter { it.findAnnotation<TaggerDocumented>() != null }
                .forEach { method ->
                    val taggerDocumentedAnnotation = method.findAnnotation<TaggerDocumented>()
                    val subArgsKey = method.findAnnotation<TaggerArgsMapping>()?.value ?: "MAIN"
                    val description =
                        taggerDocumentedAnnotation!!.description +
                                if (taggerDocumentedAnnotation.example.isNotBlank())
                                    "\nПример: ${taggerDocumentedAnnotation.example}"
                                else ""

                    documentation.computeIfAbsent(key) { HashMap() }[subArgsKey] = description
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

        for ((key, controller) in controllers) {
            if (controller.mapping(message.text, key)) {
                val messageBuilder = MessageBuilder.Builder(message.peerId)
                val dto = createDtoInstance(controller)

                for ((subArgsKey, method) in findAnnotatedMethods(controller)) {
                    subArgs[subArgsKey]?.let {
                        handleMethodInvocation(
                            method,
                            controller,
                            message,
                            it,
                            messageBuilder,
                            dto
                        )
                    }
                }

                return controller.handle(message, subArgs, messageBuilder, dto)
            }
        }

        return null
    }

    private fun createDtoInstance(controller: TaggerController<Any>): Any {
        val genericType =
            (controller.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>
        return genericType.getDeclaredConstructor().newInstance()
    }

    private fun findAnnotatedMethods(controller: TaggerController<Any>): List<Pair<String, KFunction<*>>> {
        return controller::class.memberFunctions
            .filter { it.findAnnotation<TaggerArgsMapping>() != null }
            .map { it.findAnnotation<TaggerArgsMapping>()!!.value to it }
    }

    private fun handleMethodInvocation(
        method: KFunction<*>,
        controller: TaggerController<Any>,
        message: Message,
        subArgs: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: Any
    ) {
        method.call(controller, message, subArgs, messageBuilder, dto)
    }


    fun argSplit(input: String): Map<String, Array<String>> {
        val pattern = "(-\\S+)(?:\\s(?!-\\S+)(.*?)(?=\\s-\\S+|\$))?".toRegex()
        val matches = pattern.findAll(input)
        val result = HashMap<String, Array<String>>()

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
