package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.ResendDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.UserRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMappingRole
import ru.dnlkk.tagger.service.UserService
import ru.dnlkk.tagger.util.TaggerConstants

@Component
@TaggerMappingRole([UserRole.ADMIN])
@TaggerMapping("/resend")
class ResendController @Autowired constructor(private val userService: UserService) : TaggerController<ResendDTO> {
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: ResendDTO
    ): MessageBuilder {
        if (dto.toId == null) {
            dto.toId = TaggerConstants.GROUP_ID
        }
        messageBuilder.message(dto.text)
        return messageBuilder.peerId(dto.toId!!).build()
    }

    @TaggerArgsMapping("-текст")
    fun setText(
        message: Message,
        user: User?,
        arg: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: ResendDTO
    ): MessageBuilder.Builder {
        dto.text = arg.joinToString( separator = " " ) { it }
        return messageBuilder
    }

    @TaggerArgsMapping("-кому")
    fun setUser(
        message: Message,
        user: User?,
        arg: Number,
        messageBuilder: MessageBuilder.Builder,
        dto: ResendDTO
    ): MessageBuilder.Builder {
        dto.toId = arg.toInt()
        return messageBuilder
    }
}