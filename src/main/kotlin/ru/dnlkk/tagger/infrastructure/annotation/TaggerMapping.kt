package ru.dnlkk.tagger.infrastructure.annotation
/**
 * @param value The unique name of the controller.
 *
 * If empty or not unique, then equals the name of the controller
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaggerMapping(val value: String)
