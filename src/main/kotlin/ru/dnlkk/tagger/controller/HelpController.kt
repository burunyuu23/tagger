package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.HelpDTO
import ru.dnlkk.tagger.dto.repository_dto.HelpDocDto
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.haveNeedRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.*
import ru.dnlkk.tagger.util.SpecialSymbol

@Component
@TaggerHelp
@TaggerMapping("/h")
class HelpController : TaggerController<HelpDTO> {
    private lateinit var documentation: Map<HelpDocDto, Map<HelpDocDto, String>>

    @TaggerDocumented(description = "Вызывает команду помощи")
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: HelpDTO
    ): MessageBuilder {
        dto.stringBuilder.append("Ручки бота:")

        for (docs in documentation) {
            if (!haveNeedRole(docs.key.roles, user)) {
                continue
            }
            dto.stringBuilder.append(buildDocBlock(docs, user))
        }

        return messageBuilder
            .message(dto.getHelp())
            .build()
    }

    fun buildDocBlock(docs: Map.Entry<HelpDocDto, Map<HelpDocDto, String>>, user: User?): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("\n\n")
        repeat(1) { stringBuilder.append(SpecialSymbol.WIDE_SPACE) }
        stringBuilder.append("${docs.key.path}: ")
        stringBuilder.append(
            docs.value[HelpDocDto("MAIN")]?.replace(
                "\n",
                "\n${"${SpecialSymbol.WIDE_SPACE}".repeat(2)}"
            ) ?: ""
        )
        for (doc in docs.value) {
            if (doc.key.path == "MAIN" || !haveNeedRole(doc.key.roles, user)) {
                continue
            }
            stringBuilder.append("\n")
            repeat(2) { stringBuilder.append(SpecialSymbol.WIDE_SPACE) }
            stringBuilder
                .append(
                    "${doc.key.path}: ${
                        doc.value.replace(
                            "\n",
                            "\n${"${SpecialSymbol.WIDE_SPACE}".repeat(3)}"
                        )
                    }"
                )
        }
        return stringBuilder.toString()
    }

}