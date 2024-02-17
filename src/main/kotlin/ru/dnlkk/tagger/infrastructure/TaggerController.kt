package ru.dnlkk.tagger.infrastructure

import api.longpoll.bots.model.objects.basic.Message
import ru.dnlkk.tagger.entity.User

interface TaggerController<T> {
    fun handle(message: Message, user: User?, args: Map<String, Array<String>>, messageBuilder: MessageBuilder.Builder, dto: T): MessageBuilder

    fun mapping(message: String, value: String): Boolean {
        val firstWord = message.split(" ").first()
        return firstWord == value
    }
}