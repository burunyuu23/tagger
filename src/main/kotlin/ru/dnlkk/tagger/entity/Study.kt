package ru.dnlkk.tagger.entity

import jakarta.persistence.*
import org.hibernate.validator.constraints.UniqueElements

@Entity
@Table(name = "studies")
data class Study(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = 0,

    @UniqueElements
    @Column(nullable = false)
    val name: String,

    @UniqueElements
    @Column(nullable = true)
    val url: String? = null,

    @OneToMany(mappedBy = "study")
    var lessons: List<Lesson> = ArrayList()
)
