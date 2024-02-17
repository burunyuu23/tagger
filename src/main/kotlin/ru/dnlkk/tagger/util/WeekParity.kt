package ru.dnlkk.tagger.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun calculateWeekParity(currentDate: LocalDateTime): Int {
    val startDate = LocalDate.of(2024, 2, 12)

    val days = ChronoUnit.DAYS.between(startDate, currentDate)
    val weeks = days / 7

    return weeks.toInt() % 2
}
