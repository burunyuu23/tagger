package ru.dnlkk.tagger.infrastructure.annotation

import ru.dnlkk.tagger.entity.UserRole

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class TaggerMappingRole(val value: Array<UserRole>)
