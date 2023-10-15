package ru.dnlkk.tagger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ru.dnlkk.tagger.infrastructure.front_controller.TaggerFrontController

@SpringBootApplication
class TaggerApplication {
    @Bean
    fun tagger(): Tagger {
        val tagger = Tagger(taggerProperties(), frontController())
        tagger.startPolling()
        return tagger
    }

    @Bean
    fun taggerProperties(): TaggerProperties {
        return TaggerProperties()
    }

    @Bean
    fun frontController(): TaggerFrontController {
        return TaggerFrontController()
    }
}

fun main() {
    runApplication<TaggerApplication>()
}
