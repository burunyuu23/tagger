package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.controller_dto.RaspisanieDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.UserRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMapping
import ru.dnlkk.tagger.infrastructure.annotation.TaggerMappingRole
import ru.dnlkk.tagger.service.RaspisanieFromExcelService

@Component
@TaggerMapping("/rasp")
@TaggerMappingRole([UserRole.ADMIN])
class RaspisanieFromExcelController (
    private val excelService: RaspisanieFromExcelService
): TaggerController<RaspisanieDTO> {
    @Value("classpath:raspisanie2023_2024.xlsx")
    private var resource: Resource? = null

    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: RaspisanieDTO
    ): MessageBuilder {
        if (resource == null) {
            throw Exception("Ты дурак нет такого файла!")
        }
        excelService.readExcelFromFile(resource!!)
        messageBuilder.message(message.text)
        return messageBuilder.build()
    }

}