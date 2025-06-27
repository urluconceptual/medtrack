package com.unibuc.medtrack.networking.api

import com.unibuc.medtrack.networking.models.ChatMessagesAPISendRequestModel
import com.unibuc.medtrack.networking.models.ChatMessagesAPIGetResponseModel
import com.unibuc.medtrack.networking.models.ChatMessagesAPISendResponseModel
import retrofit2.http.Body
import retrofit2.http.Query
import retrofit2.http.GET
import retrofit2.http.POST

interface ChatMessagesAPIService {
    @GET("/api/messages")
    suspend fun getChatMessages(
        @Query("p1Id") p1Id: String,
        @Query("p2Id") p2Id: String
    ) : ChatMessagesAPIGetResponseModel

    @POST("/api/messages")
    suspend fun sendChatMessage(
        @Body loginAPIRequestModel: ChatMessagesAPISendRequestModel
    ) : ChatMessagesAPISendResponseModel
}