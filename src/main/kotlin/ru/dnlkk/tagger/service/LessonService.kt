package ru.dnlkk.tagger.service

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import ru.dnlkk.tagger.dto.repository_dto.GroupDto
import ru.dnlkk.tagger.entity.*
import ru.dnlkk.tagger.repository.*
import ru.dnlkk.tagger.util.calculateWeekParity
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.jvm.optionals.getOrNull

@Service
@Transactional
class LessonService(private val lessonRepository: LessonRepository,
                    private val studyRepository: StudyRepository,
                    private val auditoriumRepository: AuditoriumRepository,
                    private val groupRepository: GroupRepository,
                    private val lectorRepository: LectorRepository,

) {

    fun getDayLessons(groupDto: GroupDto, date: LocalDateTime): List<Lesson> {
        val group = groupRepository.findByCourseAndGroupIdAndSubgroup(groupDto.course, groupDto.groupId, groupDto.subgroup)
            ?: throw Exception("Группа не найдена")
        return group.lessons.filter { it.dayOfWeek == date.dayOfWeek && it.week == calculateWeekParity(date) }
    }

    fun saveLessonWithStudy(
        name: String,
        lessonTime: LessonTime,
        week: Int,
        auditoriumId: String?,
        dayOfWeek: DayOfWeek,
        course: Int,
        groupId: String,
        subgroup: Int,
        lectorFio: String?,
    ) {
        var study = studyRepository.findByName(name)
        if (study == null) {
            study = studyRepository.save(Study(name = name))
        }
        var auditorium: Auditorium? = null
        if (auditoriumId != null) {
            auditorium = auditoriumRepository.findById(auditoriumId).getOrNull()
            if (auditorium == null) {
                auditorium = auditoriumRepository.save(Auditorium(id = auditoriumId))
            }
        }
        var group = groupRepository.findByCourseAndGroupIdAndSubgroup(course, groupId, subgroup)
        if (group == null) {
            group = groupRepository.save(Group(course = course, groupId = groupId, subgroup = subgroup))
        }
        var lector: Lector? = null
        if (lectorFio != null) {
            lector = lectorRepository.findByFio(lectorFio)
            if (lector == null) {
                lector = lectorRepository.save(Lector(fio = lectorFio))
            }
        }
        val lesson = Lesson(
            study = study,
            lessonTime = lessonTime,
            week = week,
            auditorium = auditorium,
            dayOfWeek = dayOfWeek,
            group = group,
            lector = lector
        )
        lessonRepository.save(lesson)
    }
}