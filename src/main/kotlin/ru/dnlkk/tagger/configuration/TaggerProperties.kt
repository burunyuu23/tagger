package ru.dnlkk.tagger.configuration

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TaggerProperties {
    @Value("\${tagger.access_token}")
    val accessToken: String = ""
    @Value("\${tagger.is_test}")
    val isTest: Boolean = false
}