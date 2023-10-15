package ru.dnlkk.tagger

import api.longpoll.bots.LongPollBot
import api.longpoll.bots.model.events.messages.MessageNew
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import ru.dnlkk.tagger.infrastructure.MessageBuilder
import ru.dnlkk.tagger.infrastructure.front_controller.TaggerFrontController

class Tagger(
    private val taggerProperties: TaggerProperties,
    private val taggerFrontController: TaggerFrontController
) : LongPollBot() {
    companion object {
        private val log: Logger = LoggerFactory.getLogger(this::class.java)
    }

    override fun getAccessToken(): String {
        log.info("Get access token")
        return taggerProperties.accessToken
    }

    override fun onMessageNew(messageNew: MessageNew) {
        val message = messageNew.message
        log.info(message.text)

        if (messageNew.message.peerId != 289070067)
            return

        val messageBuilder = taggerFrontController.map(message) ?: return
        buildMessage(messageBuilder)
    }

    fun buildMessage(messageBuilder: MessageBuilder) {
        messageBuilder.build(vk.messages.send())
            .execute()
    }
}

