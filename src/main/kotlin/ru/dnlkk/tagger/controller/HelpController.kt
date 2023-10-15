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
    override fun handle(
        message: Message,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: HelpDTO
    ): MessageBuilder {
        dto.stringBuilder.append("Ручки бота:")

        for (docs in documentation) {
            dto.stringBuilder.append("\n\n")
            repeat(1) { dto.stringBuilder.append("&#12288;") }
            dto.stringBuilder.append("${docs.key}: ")
            dto.stringBuilder.append(
                if (docs.value["MAIN"] != null)
                    docs.value["MAIN"]!!.replace("\n", "\n&#12288;&#12288;")
                else ""
            )
            for (doc in docs.value) {
                if (doc.key == "MAIN")
                    continue
                dto.stringBuilder.append("\n")
                repeat(2) { dto.stringBuilder.append("&#12288;") }
                dto.stringBuilder
                    .append("${doc.key}: ${doc.value.replace("\n", "\n&#12288;&#12288;&#12288;")}")
            }
        }
        return messageBuilder
            .message(dto.getHelp())
            .build()
    }

//    @TaggerArgsMapping("-р")
//    @TaggerDocumented(description = "Это просто параметр -р", example = "!h -р photo289070067_457280831")
    fun raspisanieHelp(
        message: Message,
        args: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: HelpDTO
    ): MessageBuilder.Builder {
        dto.stringBuilder.append("Help!\n\n")
        return messageBuilder.attachment(args[0])
    }

//    @TaggerArgsMapping("-с")
//    @TaggerDocumented(description = "Это просто параметр -с", example = "!h -с 2 3 4")
    fun sortHelp(
        message: Message,
        args: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: HelpDTO
    ): MessageBuilder.Builder {
        dto.stringBuilder.append("${args.map { it.toInt() }.partition { it < 4 }}\n\n")
        return messageBuilder
    }
}