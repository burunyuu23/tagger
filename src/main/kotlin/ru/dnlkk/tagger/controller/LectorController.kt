package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.UserDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.UserRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMappingRole
import ru.dnlkk.tagger.service.LectorService

@Component
@TaggerMappingRole([UserRole.ADMIN])
@TaggerMapping("/п")
class LectorController @Autowired constructor(private val lectorService: LectorService) : TaggerController<UserDTO> {
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: UserDTO
    ): MessageBuilder {
        TODO("Not yet implemented")
    }
}