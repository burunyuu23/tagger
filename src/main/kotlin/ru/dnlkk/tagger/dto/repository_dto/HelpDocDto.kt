package ru.dnlkk.tagger.dto.repository_dto

import ru.dnlkk.tagger.entity.UserRole

data class HelpDocDto(
    val path: String,
    val roles: List<UserRole>? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is HelpDocDto) return false
        return this.path == other.path
    }

    override fun hashCode(): Int {
        return path.hashCode()
    }
}
