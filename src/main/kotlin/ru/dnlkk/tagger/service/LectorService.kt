package ru.dnlkk.tagger.service

import org.springframework.stereotype.Service
import ru.dnlkk.tagger.repository.*

@Service
class LectorService(
    private val lessonRepository: LessonRepository,
    private val studyRepository: StudyRepository,
    private val auditoriumRepository: AuditoriumRepository,
    private val groupRepository: GroupRepository,
    private val lectorRepository: LectorRepository,
    ) {
}