package ru.dnlkk.tagger.dto.repository_dto

class GroupDto {
    var course: Int = 0
    var groupId: String = "0"
    var subgroup: Int = 0

    constructor(course: Int, groupId: String, subgroup: Int) {
        this.course = course
        this.groupId = groupId
        this.subgroup = subgroup
    }

    constructor()

    override fun toString(): String {
        return "GroupDto(course=$course, groupId='$groupId', subgroup=$subgroup)"
    }


}
