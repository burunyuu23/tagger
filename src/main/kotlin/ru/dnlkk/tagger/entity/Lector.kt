package ru.dnlkk.tagger.entity

import jakarta.persistence.*
import org.hibernate.validator.constraints.UniqueElements

@Entity
@Table(name = "lectors")
data class Lector(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @UniqueElements
    @Column(nullable = false)
    val fio: String,

    @OneToMany(mappedBy = "lector")
    val lessons: List<Lesson> = ArrayList()
)
