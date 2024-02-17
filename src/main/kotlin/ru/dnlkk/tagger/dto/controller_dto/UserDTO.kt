package ru.dnlkk.tagger.dto.controller_dto

import ru.dnlkk.tagger.dto.repository_dto.GroupDto
import ru.dnlkk.tagger.entity.UserRole

class UserDTO {
    var id: Long = 0L
    var zachetka: Long? = null
    var roles: List<UserRole> = ArrayList()
    var groupDto: GroupDto? = null
}