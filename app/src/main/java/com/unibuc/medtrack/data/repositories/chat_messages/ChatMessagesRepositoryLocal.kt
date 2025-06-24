package com.unibuc.medtrack.data.repositories.chat_messages

import com.unibuc.medtrack.data.dao.ChatMessagesDAO
import com.unibuc.medtrack.data.models.ChatMessageModel
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepository

class ChatMessagesRepositoryLocal(val dao: ChatMessagesDAO): ChatMessagesRepository {
    override suspend fun insert(chatMessages: ChatMessageModel) = dao.insert(chatMessages)

    override suspend fun getAllByParticipants(
        p1: String,
        p2: String
    ): List<ChatMessageModel> = dao.getAllByParticipants(p1, p2)

    override suspend fun getAllMyConversationUserIds(myId: String)
    : List<String> = dao.getAllMyConversationUserIds(myId)

    override suspend fun updateChatMessage(chatMessage: ChatMessageModel) {
        dao.update(chatMessage)
    }
}