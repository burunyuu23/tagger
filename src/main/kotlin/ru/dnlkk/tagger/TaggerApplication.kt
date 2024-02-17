package ru.dnlkk.tagger

import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.ZoneId
import java.util.*

@SpringBootApplication
class TaggerApplication {
    @PostConstruct
    fun setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")))
    }
}

fun main(args: Array<String>) {
    runApplication<TaggerApplication>(*args)
}
