package ru.dnlkk.tagger.infrastructure.annotation

@Target(AnnotationTarget.FUNCTION)
annotation class TaggerDocumented(val description: String, val example: String = "")
