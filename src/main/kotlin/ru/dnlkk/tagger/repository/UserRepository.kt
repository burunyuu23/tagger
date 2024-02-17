package ru.dnlkk.tagger.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.dnlkk.tagger.entity.User

@Repository
interface UserRepository: JpaRepository<User, Int> {
}