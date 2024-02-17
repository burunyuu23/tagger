package ru.dnlkk.tagger

import jakarta.annotation.PostConstruct
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
        properties: TaggerProperties,
        taggerFrontController: TaggerFrontController
    ) = Tagger(properties, taggerFrontController).apply {
        startPolling()
    }

    @Bean
    fun properties() = TaggerProperties()

    @Bean
    fun frontController() = TaggerFrontController()

    @PostConstruct
    fun setDefaultTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Europe/Moscow")))
    }
}

fun main(args: Array<String>) {
    runApplication<TaggerApplication>(*args)
}
