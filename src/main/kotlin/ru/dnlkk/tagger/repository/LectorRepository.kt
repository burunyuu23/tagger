package ru.dnlkk.tagger.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.dnlkk.tagger.entity.Lector

@Repository
interface LectorRepository: JpaRepository<Lector, Int> {
    fun findByFio(fio: String): Lector?
}