package ru.dnlkk.tagger.infrastructure.annotation

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaggerArgsMapping(val value: String)
