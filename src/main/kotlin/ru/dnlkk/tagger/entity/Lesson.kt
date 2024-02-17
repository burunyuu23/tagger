package ru.dnlkk.tagger.entity

import DayOfWeekConverter
import jakarta.persistence.*
import java.time.DayOfWeek

@Entity
@Table(name = "lessons")
data class Lesson(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "lesson_time")
    val lessonTime: LessonTime,

    @Convert(converter = DayOfWeekConverter::class)
    @Column(nullable = false, name = "day_of_week")
    val dayOfWeek: DayOfWeek,

    @Column(nullable = false, name = "week")
    val week: Int,

    @ManyToOne
    @JoinColumn(name = "auditorium_id")
    val auditorium: Auditorium?,

    @ManyToOne
    @JoinColumn(name = "lector_id")
    val lector: Lector?,

    @ManyToOne
    @JoinColumn(name = "study_id")
    val study: Study,

    @ManyToOne
    @JoinColumn(name = "group_id")
    val group: Group,
)