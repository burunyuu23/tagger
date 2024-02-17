package ru.dnlkk.tagger

import api.longpoll.bots.LongPollBot
import api.longpoll.bots.model.events.messages.MessageNew
import jakarta.annotation.PostConstruct
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import ru.dnlkk.tagger.configuration.TaggerProperties
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.front_controller.TaggerFrontController

@Component
class Tagger(
    private val taggerProperties: TaggerProperties,
    private val taggerFrontController: TaggerFrontController
) : LongPollBot() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    @PostConstruct
    fun start() = startPolling()

    override fun getAccessToken(): String {
        log.info("Get access token")
        return taggerProperties.accessToken
    }

    override fun onMessageNew(messageNew: MessageNew) {
        val message = messageNew.message
        log.info(message.text)

        if (taggerProperties.isTest && messageNew.message.peerId !in setOf(289070067, 283369041, 328597719))
            return

        val messageBuilder = taggerFrontController.dispatch(message) ?: return
        buildMessage(messageBuilder)
    }

    fun buildMessage(messageBuilder: MessageBuilder) {
        messageBuilder.build(vk.messages.send())
            .executeAsync()
    }
}

