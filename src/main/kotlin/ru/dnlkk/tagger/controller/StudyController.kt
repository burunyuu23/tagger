package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.repository_dto.GroupDto
import ru.dnlkk.tagger.dto.controller_dto.RaspisanieDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.UserRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerArgsMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerDocumented
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMappingRole
import ru.dnlkk.tagger.service.LessonService
import ru.dnlkk.tagger.util.raspisanieLessonsFormat

@Component
@TaggerMappingRole([UserRole.ADMIN])
@TaggerMapping("/study")
class StudyController @Autowired constructor(private val lessonService: LessonService) : TaggerController<RaspisanieDTO> {

    @TaggerDocumented(description = "Добавить занятию данных")
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: RaspisanieDTO
    ): MessageBuilder {
        return messageBuilder.build()
    }
}