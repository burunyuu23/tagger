package ru.dnlkk.tagger.entity

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    val id: Long,

    @Column(nullable = true, name = "zachetka")
    val zachetka: Long?,

    @ElementCollection(targetClass = UserRole::class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = [JoinColumn(name = "user_id")])
    @Column(name = "role")
    val roles: List<UserRole> = mutableListOf(),

    @ManyToOne
    @JoinColumn(name = "group_id")
    val group: Group?,
)

fun haveNeedRole(needRoles: List<UserRole>?, user: User?): Boolean {
    return !(needRoles != null && (user == null || (needRoles.isNotEmpty() && !user.roles.any { it in needRoles })))
}

