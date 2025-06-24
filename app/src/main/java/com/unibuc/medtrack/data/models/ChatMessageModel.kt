package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "chat_messages")
data class ChatMessageModel(
    @PrimaryKey
    val id: String,
    val senderId: String,
    val receiverId: String,
    val message: String,
    val timeSent: String
)