package ru.dnlkk.tagger.controller

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.runner.ApplicationContextRunner

class TestControllerTest {

    private val clazz = TestController::class.java

    private val contextRunner = ApplicationContextRunner()
        .withUserConfiguration(TestController::class.java)

    @Test
    fun `positive - is_test=true`() {
        contextRunner
            .withPropertyValues("tagger.is_test=true")
            .run { assertThat(it).hasSingleBean(clazz) }
    }

    @Test
    fun `negative - is_test=false`() {
        contextRunner
            .withPropertyValues("tagger.is_test=false")
            .run { assertThat(it).doesNotHaveBean(clazz) }
    }

    @Test
    fun `negative - is_test not provided`() {
        contextRunner
            .run { assertThat(it).doesNotHaveBean(clazz) }
    }
}
