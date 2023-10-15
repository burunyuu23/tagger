package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.HelpDTO
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerDocumented
import ru.dnlkk.tagger.infrastructure.annotation.TaggerHelp
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping

@Component
@TaggerHelp
@TaggerMapping("!h")
class HelpController: TaggerController<HelpDTO> {
    private lateinit var documentation: Map<String, Map<String, String>>

    @TaggerDocumented(description = "Вызывает команду помощи")
    override fun handle(message: Message, args: Map<String, Array<String>>, messageBuilder: MessageBuilder.Builder, dto: HelpDTO): MessageBuilder {
        dto.stringBuilder.append("Ручки бота:\n")

        for ( docs in documentation ) {
            repeat(1) {dto.stringBuilder.append("&#12288;")}
            dto.stringBuilder.append("${docs.key}: ")
            dto.stringBuilder.append(if (docs.value["MAIN"] != null) "${docs.value["MAIN"]}\n" else "\n")
            for (doc in docs.value) {
                repeat(2) {dto.stringBuilder.append("&#12288;")}
                dto.stringBuilder
                    .append(if (doc.key != "MAIN") "${doc.key}: ${doc.value}\n" else "")
            }
        }
        return messageBuilder
            .message(dto.getHelp())
            .build()
    }

    @TaggerArgsMapping("-р")
    @TaggerDocumented(description = "Это просто параметр -р", example = "!h -р")
    fun raspisanieHelp(message: Message, args: Array<String>, messageBuilder: MessageBuilder.Builder, dto: HelpDTO): MessageBuilder.Builder {
        dto.stringBuilder.append("Help!\n\n")
        return messageBuilder
    }
}