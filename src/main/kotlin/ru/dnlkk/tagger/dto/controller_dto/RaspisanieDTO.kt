package ru.dnlkk.tagger.dto.controller_dto

import ru.dnlkk.tagger.dto.repository_dto.GroupDto
import java.time.LocalDateTime

class RaspisanieDTO {
    var group: GroupDto? = null
    var localDateTime: LocalDateTime = LocalDateTime.now()
}
