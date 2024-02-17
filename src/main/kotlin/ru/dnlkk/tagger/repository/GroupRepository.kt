package ru.dnlkk.tagger.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import ru.dnlkk.tagger.entity.Group

@Repository
interface GroupRepository: JpaRepository<Group, Int>  {
    fun findByCourseAndGroupIdAndSubgroup(course: Int, groupId: String, subgroup: Int): Group?
}