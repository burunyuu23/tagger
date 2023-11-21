package ru.dnlkk.tagger.infrastructure

import api.longpoll.bots.model.objects.basic.Message

interface TaggerController<T> {
    fun handle(message: Message, args: Map<String, Array<String>>, messageBuilder: MessageBuilder.Builder, dto: T): MessageBuilder

    fun mapping(message: String, value: String): Boolean {
        val firstWord = message.split(" ").first()
        return firstWord == value
    }
}