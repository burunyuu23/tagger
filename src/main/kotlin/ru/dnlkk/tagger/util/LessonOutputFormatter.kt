package ru.dnlkk.tagger.util

import ru.dnlkk.tagger.entity.Lesson
import ru.dnlkk.tagger.entity.LessonTime
import ru.dnlkk.tagger.entity.Study

fun raspisanieLessonsFormat(lessons: List<Lesson>): List<Lesson> {
    if (lessons.isEmpty()) {
        return lessons
    }
    val lessonsTime = ArrayList<Lesson>()
    val lastLesson = lessons.last()
    for (lessonTime in LessonTime.values()) {
        if (lessonTime.startTime > lastLesson.lessonTime.endTime) {
            return lessonsTime
        }
        val lesson = lessons.find { it.lessonTime.startTime == lessonTime.startTime }
        lessonsTime.add(
            lesson ?: Lesson(
                lessonTime = lessonTime,
                study = Study(name = "Окно"),
                auditorium = null,
                lector = null,
                dayOfWeek = lastLesson.dayOfWeek,
                week = lastLesson.week,
                group = lastLesson.group
            )
        )
    }
    return lessons
}