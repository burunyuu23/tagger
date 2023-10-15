package ru.dnlkk.tagger.dto.controller_dto

class HelpDTO {
    val stringBuilder = StringBuilder()

    fun getHelp(): String {
        return stringBuilder.toString()
    }

    val map = mapOf(1 to 2)
}
