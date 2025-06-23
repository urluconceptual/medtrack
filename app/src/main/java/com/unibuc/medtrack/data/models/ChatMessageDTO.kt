package com.unibuc.medtrack.data.models

import java.util.Date

data class ChatMessageDTO(
    val message: String,
    val sentByMe: Boolean,
    val timeSent: Date
)