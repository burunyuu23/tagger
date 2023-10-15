package ru.dnlkk.tagger.infrastructure.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaggerDocumented(val description: String, val example: String = "[null]")
