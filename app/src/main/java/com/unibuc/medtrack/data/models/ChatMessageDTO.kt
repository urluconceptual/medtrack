package com.unibuc.medtrack.data.models

data class ChatMessageDTO(
    val message: String,
    val sentByMe: Boolean,
    val timeSent: String
) {
    companion object {
        fun fromModel(model: ChatMessageModel, currentUserId: String): ChatMessageDTO {
            return ChatMessageDTO(
                message = model.message,
                sentByMe = (model.senderId == currentUserId),
                timeSent = model.timeSent
            )
        }
    }
}