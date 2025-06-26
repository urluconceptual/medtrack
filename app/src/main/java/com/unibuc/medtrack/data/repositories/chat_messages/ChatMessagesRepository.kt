package com.unibuc.medtrack.data.repositories.chat_messages

import com.unibuc.medtrack.data.models.ChatMessageModel

interface ChatMessagesRepository {
    suspend fun insert(chatMessages: ChatMessageModel)
    suspend fun getAllByParticipants(p1: String, p2: String): List<ChatMessageModel>
    suspend fun getAllMyConversationUserIds(myId: String): List<String>
    suspend fun updateChatMessage(chatMessage: ChatMessageModel)
}