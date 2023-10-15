package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.HelpDTO
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping

@Component
@TaggerMapping("!h")
class HelpController: TaggerController<HelpDTO> {

    override fun handle(message: Message, args: Map<String, Array<String>>, messageBuilder: MessageBuilder.Builder, dto: HelpDTO): MessageBuilder {
        dto.stringBuilder.append("Me please!")
        return messageBuilder
            .message(messageBuilder.message + "world!" + dto.getHelp())
            .build()
    }

    @TaggerArgsMapping("-р")
    fun raspisanieHelp(message: Message, args: Array<String>, messageBuilder: MessageBuilder.Builder, dto: HelpDTO): MessageBuilder.Builder {
        dto.stringBuilder.append("Help! ")
        return messageBuilder.message("Hello, ")
    }

}