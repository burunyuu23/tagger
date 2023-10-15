package ru.dnlkk.tagger.infrastructure

import api.longpoll.bots.model.objects.basic.Message

interface TaggerController<T> {
    fun handle(message: Message, args: Map<String, Array<String>>, messageBuilder: MessageBuilder.Builder, dto: T): MessageBuilder
}