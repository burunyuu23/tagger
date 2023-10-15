package ru.dnlkk.tagger.infrastructure

import api.longpoll.bots.methods.impl.messages.Send
import java.io.File

class MessageBuilder private constructor(
    val peerId: Int,
    val message: String?,
    val attachments: List<File>?,
    val stickerId: Int?,
    val photo: File?,
) {

    private constructor(peerId: Int) : this(peerId, null, null, null, null)

    class Builder(private val peerId: Int) {
        var message: String? = ""
            private set
        var attachments: List<File>? = null
            private set
        var stickerId: Int? = null
            private set
        var photo: File? = null
            private set

        fun message(message: String?) = apply { this.message = message }
        fun attachments(attachments: List<File>?) = apply { this.attachments = attachments }
        fun stickerId(stickerId: Int?) = apply { this.stickerId = stickerId }
        fun photo(photo: File?) = apply { this.photo = photo }

        fun build() = MessageBuilder(peerId, message, attachments, stickerId, photo)
    }

    fun build(send: Send): Send {
        if (stickerId != null)
            send.setStickerId(stickerId)
        if (message != null)
            send.setMessage(message)
        if (photo != null)
            send.addPhoto(photo)

        return send.setPeerId(peerId)
    }
}