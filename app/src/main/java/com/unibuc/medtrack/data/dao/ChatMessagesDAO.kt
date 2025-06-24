package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unibuc.medtrack.data.models.ChatMessageModel

@Dao
interface ChatMessagesDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(chatMessages: ChatMessageModel)

    @Query(
        """
        SELECT *
        FROM chat_messages
        WHERE (senderId = :p1
            AND receiverId = :p2)
            OR
            (senderId = :p2
            AND receiverId = :p1)
        ORDER BY timeSent
    """
    )
    suspend fun getAllByParticipants(p1: String, p2: String): List<ChatMessageModel>

    @Update
    suspend fun update(chatMessage: ChatMessageModel)


    @Query(
        """
        SELECT DISTINCT *
        FROM
            (SELECT senderId
            FROM chat_messages
            WHERE receiverId = :myId
            UNION
            SELECT receiverId
            FROM chat_messages
            WHERE senderId = :myId)
    """
    )
    suspend fun getAllMyConversationUserIds(myId: String): List<String>
}