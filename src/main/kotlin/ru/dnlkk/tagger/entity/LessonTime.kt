package ru.dnlkk.tagger.entity

import java.time.LocalTime

enum class LessonTime(val text: String, val startTime: LocalTime, val endTime: LocalTime) {
    // 🕐
    FIRST_LESSON("&#128336;",
    LocalTime.of(8, 0),
        LocalTime.of(9, 35)),
    // 🕑
    SECOND_LESSON("&#128337;",
    LocalTime.of(9, 45),
    LocalTime.of(11, 20)),
    // 🕒
    THIRD_LESSON("&#128338;",
    LocalTime.of(11,30),
    LocalTime.of(13, 5)),
    // 🕓
    FOURTH_LESSON("&#128339;",
    LocalTime.of(13, 25),
    LocalTime.of(15, 0)),
    // 🕔
    FIFTH_LESSON("&#128340;",
    LocalTime.of(15, 10),
    LocalTime.of(16, 45)),
    // 🕕
    SIXTH_LESSON("&#128341;",
    LocalTime.of(16, 55),
    LocalTime.of(18, 30)),
    // 🕖
    SEVENTH_LESSON("&#128342;",
    LocalTime.of(18,40),
    LocalTime.of(20, 0)),
    // 🕗
    EIGHTH_LESSON("&#128343;",
    LocalTime.of(20, 10),
    LocalTime.of(21, 30));
}
