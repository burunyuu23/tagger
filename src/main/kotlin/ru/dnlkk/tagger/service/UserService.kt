package ru.dnlkk.tagger.service

import org.springframework.stereotype.Service
import ru.dnlkk.tagger.dto.controller_dto.UserDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.repository.GroupRepository
import ru.dnlkk.tagger.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
    private val groupRepository: GroupRepository,
) {
    fun saveUser(dto: UserDTO) {
        if (dto.groupDto == null) {
            throw Exception("Не понял че за группа")
        }
        val group = groupRepository.findByCourseAndGroupIdAndSubgroup(dto.groupDto!!.course, dto.groupDto!!.groupId, dto.groupDto!!.subgroup)
        userRepository.save(User(id = dto.id, group = group, zachetka = dto.zachetka, roles = dto.roles))
    }
}