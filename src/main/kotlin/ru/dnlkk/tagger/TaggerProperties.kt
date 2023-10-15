package ru.dnlkk.tagger

import org.springframework.beans.factory.annotation.Value

class TaggerProperties {
    @Value("\${tagger.access_token}")
    val accessToken: String = ""
}