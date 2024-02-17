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
@TaggerMapping("/р")
class LessonController @Autowired constructor(private val lessonService: LessonService) : TaggerController<RaspisanieDTO> {

    @TaggerDocumented(description = "Показывает расписание. Без параметров показывает расписание на сегодня")
    override fun handle(
        message: Message,
        user: User?,
        args: Map<String, Array<String>>,
        messageBuilder: MessageBuilder.Builder,
        dto: RaspisanieDTO
    ): MessageBuilder {
        if (dto.group == null && user?.group == null) {
            throw Exception("Чето непонятно для какой группы брать расписание")
        }
        var group = dto.group
        if (dto.group == null && user?.group != null) {
            group = GroupDto(user.group.course, user.group.groupId, user.group.subgroup)
        }
        messageBuilder.message(
            group?.let { groupDto ->
                raspisanieLessonsFormat(lessonService.getDayLessons(
                    groupDto,
                    dto.localDateTime,
                )).joinToString(separator = "\n\n") {
                    "${it.lessonTime.text} ${it.lessonTime.startTime} : ${it.lessonTime.endTime} - ${it.study.name} ${it.lector?.fio ?: ""} ${it.auditorium?.id ?: ""}"
                }
                    .trimIndent()
            }
        )
        return messageBuilder.build()
    }

    @TaggerArgsMapping("-группа")
    @TaggerDocumented(
        description = "Для какой группы брать расписание, если вы зарегистрированный человек, то по дефолту для вас будет браться ваша группа. Формат: курс группа подгруппа",
        example = "/р -группа 3 7 2"
    )
    fun group(
        message: Message,
        user: User?,
        args: Array<String>,
        messageBuilder: MessageBuilder.Builder,
        dto: RaspisanieDTO
    ): MessageBuilder.Builder {
        val group = user?.group
        dto.group = GroupDto().apply {
            subgroup = args[args.lastIndex].toIntOrNull() ?: group?.subgroup ?: subgroup
            groupId = if (args.size >= 2) args[args.lastIndex - 1] else group?.groupId ?: groupId
            course = if (args.size >= 3) args[args.lastIndex - 2].toIntOrNull() ?: course else  group?.course ?:course
        }
        if (args.size !in 1..3) {
            throw Exception("А группа то голая")
        }
        return messageBuilder
    }

    @TaggerArgsMapping("-дата")
    @TaggerDocumented(
        description = "Устанавливает дату для расписания на указанное число. Если введено только число берёт его как день текущего месяца, если введено число число берёт его как день и месяц. Пример выдаст расписание для 14го февраля",
        example = "/р -дата 14 2"
    )
    fun date(
        message: Message,
        user: User?,
        args: Array<Int>,
        messageBuilder: MessageBuilder.Builder,
        dto: RaspisanieDTO
    ): MessageBuilder.Builder {
        if (args.size > 2 || args.isEmpty()) {
            throw Exception("Напортачили вы с аргументами")
        } else if (args.size == 2) {
            dto.localDateTime = dto.localDateTime.withMonth(args[1]).withDayOfMonth(args[0])
        } else {
            dto.localDateTime = dto.localDateTime.withDayOfMonth(args[0])
        }
        return messageBuilder
    }
}