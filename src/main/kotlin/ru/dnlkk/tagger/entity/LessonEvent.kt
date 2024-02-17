package ru.dnlkk.tagger.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "events")
data class LessonEvent(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = true)
    val description: String,

    @Column(name = "time_start",nullable = false)
    val timeStart: LocalDateTime,
)
