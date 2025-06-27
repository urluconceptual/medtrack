package com.unibuc.medtrack.networking.models

data class ChatMessagesAPISendRequestModel(
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timeSent: String
)