package ru.dnlkk.tagger.entity

import jakarta.persistence.*

@Entity
@Table(name = "groups")
data class Group(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @Column(nullable = false)
    var course: Int = 0,

    @Column(nullable = false, name = "group_id")
    var groupId: String = "",

    @Column(nullable = false)
    var subgroup: Int = 0,

    @OneToMany(mappedBy = "group")
    var lessons: List<Lesson> = ArrayList(),

    @OneToMany(mappedBy = "group")
    var users: List<User> = ArrayList()
)
