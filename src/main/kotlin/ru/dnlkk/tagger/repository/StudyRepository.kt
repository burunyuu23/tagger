package ru.dnlkk.tagger.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.dnlkk.tagger.entity.Study

@Repository
interface StudyRepository : JpaRepository<Study, Int> {
    fun findByName(name: String): Study?
}