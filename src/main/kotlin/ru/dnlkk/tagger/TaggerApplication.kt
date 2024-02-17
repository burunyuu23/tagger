package ru.dnlkk.tagger

import jakarta.annotation.PostConstruct
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.dnlkk.tagger.configuration.TaggerProperties
import ru.dnlkk.tagger.infrastructure.front_controller.TaggerFrontController
import java.time.ZoneId
import java.util.*

@SpringBootApplication
class TaggerApplication {

    @Bean
    fun taggerBean(
        @Qualifier("taggerProperties") taggerProperties: TaggerProperties,
        @Qualifier("taggerFrontController") taggerFrontController: TaggerFrontController
    ) = Tagger(taggerProperties, taggerFrontController).apply {
        startPolling()
    }

    @PostConstruct
    fun setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")))
    }
}

fun main(args: Array<String>) {
    runApplication<TaggerApplication>(*args)
}
