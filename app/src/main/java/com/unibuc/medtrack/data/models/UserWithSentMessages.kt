package com.unibuc.medtrack.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithSentMessages(
    @Embedded val user: UserModel,
    @Relation(
        parentColumn = "userId",
        entityColumn = "senderId"
    )
    val sentMessages: List<ChatMessageModel>
)