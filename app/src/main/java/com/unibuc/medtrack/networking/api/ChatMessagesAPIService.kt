package com.unibuc.medtrack.networking.api

import com.unibuc.medtrack.networking.models.ChatMessagesAPIResponseModel
import retrofit2.http.Query
import retrofit2.http.GET

interface ChatMessagesAPIService {
    @GET("/api/messages")
    suspend fun getChatMessages(
        @Query("p1Id") p1Id: String,
        @Query("p2Id") p2Id: String
    ) : ChatMessagesAPIResponseModel
}