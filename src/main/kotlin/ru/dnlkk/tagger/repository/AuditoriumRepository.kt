package ru.dnlkk.tagger.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.dnlkk.tagger.entity.Auditorium

@Repository
interface AuditoriumRepository : JpaRepository<Auditorium, String> {
}