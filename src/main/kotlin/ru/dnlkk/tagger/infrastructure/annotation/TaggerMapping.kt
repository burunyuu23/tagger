package ru.dnlkk.tagger.infrastructure.annotation

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaggerMapping(val value: String)
