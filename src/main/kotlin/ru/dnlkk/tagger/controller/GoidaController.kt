package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.GoidaDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerDocumented
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping

@Component
@TaggerMapping(value = "гойда")
class GoidaController: TaggerController<GoidaDTO> {
    private val regex = Regex("^[г|Г]+\\s*[о|О|0|o|O]+\\s*[й|Й]+\\s*[д|Д]+\\s*[а|А|a|A]+.*\$")

    override fun mapping(message: String, value: String): Boolean {
        return regex.containsMatchIn(message)
    }

    @TaggerDocumented(
        description = "ЭТО НАШ РОДНОЙ МЕТОД Г-Г-ГООоЙйЙДддАаАааАА.",
        example = "ГГГГгоооОO0ОоoоООЙйййЙДДдДдДААaААаaАAА"
    )
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: GoidaDTO
    ): MessageBuilder {
        return messageBuilder
            .message(message.text.uppercase().plus("!".repeat(message.text.length)))
            .attachment("photo-207300901_457239033")
            .build()
    }
}