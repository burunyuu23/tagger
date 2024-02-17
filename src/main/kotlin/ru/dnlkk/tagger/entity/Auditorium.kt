package ru.dnlkk.tagger.entity

import jakarta.persistence.*

@Entity
@Table(name = "auditoriums")
data class Auditorium(
    @Id
    val id: String,

    @OneToMany(mappedBy = "auditorium")
    val lessons: List<Lesson> = ArrayList()
)
