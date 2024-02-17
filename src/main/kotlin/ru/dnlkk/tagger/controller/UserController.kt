package ru.dnlkk.tagger.controller

import api.longpoll.bots.model.objects.basic.Message
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.dto.repository_dto.GroupDto

import ru.dnlkk.tagger.dto.controller_dto.UserDTO
import ru.dnlkk.tagger.entity.User
import ru.dnlkk.tagger.entity.UserRole
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.TaggerController
import ru.dnlkk.tagger.infrastructure.annotation.*
import ru.dnlkk.tagger.service.UserService

@Component
@TaggerMappingRole([UserRole.ADMIN])
@TaggerMapping("/users")
class UserController @Autowired constructor(private val userService: UserService) : TaggerController<UserDTO> {

    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: UserDTO
    ): MessageBuilder {
        userService.saveUser(dto)
        return messageBuilder.build()
    }

    @TaggerArgsMapping("-добавить")
    @TaggerDocumented(
        description = "Добавить user в базу",
        example = "/users -добавить 289070067"
    )
    fun addUser(
        message: Message,
        user: User?,
        arg: Number,
        messageBuilder: MessageBuilder.Builder,
        dto: UserDTO
    ): MessageBuilder.Builder {
        dto.id = arg.toLong()
        return messageBuilder
    }

    @TaggerArgsMapping("-группа")
    @TaggerDocumented(
        description = "Добавить группу для user",
        example = "/users -добавить 289070067 -группа 3 7 2"
    )
    fun addUserGroup(
        message: Message,
        user: User?,
        args: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: UserDTO
    ): MessageBuilder.Builder {
        if (args.size !in 1..3) {
            throw Exception("Неправильно указана группа")
        }
        val group = user?.group
        dto.groupDto = GroupDto().apply {
            subgroup = args[args.lastIndex].toIntOrNull() ?: group?.subgroup ?: subgroup
            groupId = if (args.size >= 2) args[args.lastIndex - 1] else group?.groupId ?: groupId
            course = if (args.size >= 3) args[args.lastIndex - 2].toIntOrNull() ?: course else  group?.course ?:course
        }
        return messageBuilder
    }

    @TaggerArgsMapping("-роль")
    @TaggerDocumented(
        description = "Добавить роль для user (использовать только вместе с -добавить)",
        example = "/users -добавить 289070067 -роль ADMIN"
    )
    fun addUserRole(
        message: Message,
        user: User?,
        args: Array<UserRole>,
        messageBuilder: MessageBuilder.Builder,
        dto: UserDTO
    ): MessageBuilder.Builder {
        dto.roles = args.toList()
        return messageBuilder
    }
}