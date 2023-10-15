package ru.dnlkk.tagger.infrastructure.front_controller

import api.longpoll.bots.model.objects.basic.Message
import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.memberFunctions

@Component
class TaggerFrontController {
    @Autowired
    private lateinit var context: ApplicationContext
    private val controllers = HashMap<String, TaggerController<Any>>()

    @PostConstruct
    private fun init() {
        val beans = this.context.getBeansWithAnnotation(TaggerMapping::class.java)
        for (bean in beans) {
            val clazz: KClass<*> = bean.value::class
            val annotation = clazz.findAnnotation<TaggerMapping>()
            controllers[annotation!!.value] = bean.value as TaggerController<Any>
        }
    }

    fun map(message: Message): MessageBuilder? {
        val args = message.text.split(" ")
        val subArgs = argSplit(message.text)

        for (controller in controllers) {
            if (args.first() in controller.key) {
                var messageBuilder = MessageBuilder.Builder(message.peerId)
                val c = controller.value
                val dto = ((c.javaClass.genericInterfaces[0] as ParameterizedType).actualTypeArguments[0] as Class<*>).getDeclaredConstructor().newInstance()
                for (subArgsKey in subArgs.keys) {
                    val annotatedMethods =
                        c::class.memberFunctions.filter { it.findAnnotation<TaggerArgsMapping>() != null }
                    for (method in annotatedMethods) {
                        val annotation = method.findAnnotation<TaggerArgsMapping>()
                        if (annotation!!.value == subArgsKey)
                            messageBuilder =
                                method.call(c, message, subArgs[subArgsKey], messageBuilder, dto) as MessageBuilder.Builder
                    }
                }
                return controller.value.handle(message, subArgs, messageBuilder, dto)
            }
        }
        return null
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