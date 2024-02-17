package ru.dnlkk.tagger.infrastructure

import api.longpoll.bots.methods.impl.messages.Send
import java.io.File

class MessageBuilder private constructor(
    val peerId: Int,
    val message: String?,
    val attachment: String?,
    val stickerId: Int?,
    val photo: File?,
) {

    class Builder(private var peerId: Int) {
        var message: String? = ""
            private set
        var attachment: String? = null
            private set
        var stickerId: Int? = null
            private set
        var photo: File? = null
            private set

        fun peerId(peerId: Int) = apply { this.peerId = peerId }
        fun message(message: String?) = apply { this.message += message }
        fun messageNewLine(message: String?) = apply { this.message += "\n$message" }
        fun clearMessage(message: String?) = apply { this.message = message }
        fun attachment(attachment: String?) = apply { this.attachment = attachment }
        fun stickerId(stickerId: Int?) = apply { this.stickerId = stickerId }
        fun photo(photo: File?) = apply { this.photo = photo }

        fun build() = MessageBuilder(peerId, message, attachment, stickerId, photo)
    }

    fun build(send: Send): Send {
        stickerId?.let { send.setStickerId(it) }
        message?.let { send.setMessage(it) }
        photo?.let { send.addPhoto(it) }
        attachment?.let { send.setAttachment(it) }
        return send.setPeerId(peerId)
    }

    override fun toString(): String {
        return "MessageBuilder(peerId=$peerId, message=$message, attachment=$attachment, stickerId=$stickerId, photo=$photo)"
    }


}