package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.TestDTO
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerDocumented
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping

@Component
@TaggerMapping("!test")
class TestController: TaggerController<TestDTO> {

    @TaggerDocumented(description = "Вызывает команду test")
    override fun handle(
        message: Message,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: TestDTO
    ): MessageBuilder {
        return messageBuilder.build()
    }

    @TaggerArgsMapping("-сум")
    @TaggerDocumented(description = "Это просто параметр -сум", example = "!test -сум 2 3 4")
    fun sortHelp(
        message: Message,
        args: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: TestDTO
    ): MessageBuilder.Builder {
        return messageBuilder.message(args.map { it.toInt() }.reduce {acc, i -> acc + i}.toString())
    }
}