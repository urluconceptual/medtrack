package com.unibuc.medtrack.networking.models

import com.unibuc.medtrack.data.models.ChatMessageModel

data class ChatMessagesAPIResponseModel(
    val data: List<ChatMessageModel>
)