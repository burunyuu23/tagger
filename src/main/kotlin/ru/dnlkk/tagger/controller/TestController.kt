package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.TestDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.UserRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerDocumented
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMappingRole

@ConditionalOnProperty(prefix = "tagger", name = ["is_test"])
@Component
@TaggerMapping("/test")
@TaggerMappingRole([UserRole.ADMIN, UserRole.TEST])
class TestController : TaggerController<TestDTO> {

    @TaggerDocumented(description = "Вызывает команду test")
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: TestDTO
    ): MessageBuilder {
        return messageBuilder.build()
    }

    @TaggerArgsMapping("-сум")
    @TaggerDocumented(description = "Это просто параметр -сум", example = "!test -сум 2.2 3.3 4.1")
    fun sortHelp(
        message: Message,
        user: User?,
        args: Array<Double>,
        messageBuilder: MessageBuilder.Builder,
        dto: TestDTO
    ): MessageBuilder.Builder {
        return messageBuilder.message(args.reduce { acc, i -> acc + i }.toString())
    }

    @TaggerArgsMapping("-пг")
    @TaggerDocumented(description = "Это просто параметр -пг", example = "!test -пг")
    fun pgTest(
        message: Message,
        user: User?,
        arg: Boolean,
        messageBuilder: MessageBuilder.Builder,
        dto: TestDTO
    ): MessageBuilder.Builder {
        dto.isPg = arg
        return messageBuilder.message("Да да да!!!")
    }
}