package ru.dnlkk.tagger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.dnlkk.tagger.configuration.TaggerProperties
import ru.dnlkk.tagger.infrastructure.front_controller.TaggerFrontController

@SpringBootApplication
class TaggerApplication {

    @Bean
    fun tagger(
        properties: TaggerProperties,
        taggerFrontController: TaggerFrontController
    ) = Tagger(properties, taggerFrontController).apply {
        startPolling()
    }

    @Bean
    fun taggerProperties() = TaggerProperties()

    @Bean
    fun frontController() = TaggerFrontController()
}

fun main() {
    runApplication<TaggerApplication>()
}
