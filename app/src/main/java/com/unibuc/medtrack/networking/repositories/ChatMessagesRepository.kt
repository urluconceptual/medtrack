package com.unibuc.medtrack.networking.repositories

import com.unibuc.medtrack.networking.api.ChatMessagesAPIService
import com.unibuc.medtrack.networking.client.RetrofitClient
import com.unibuc.medtrack.networking.models.ChatMessagesAPISendRequestModel
import com.unibuc.medtrack.networking.models.ChatMessagesAPIGetResponseModel
import com.unibuc.medtrack.networking.models.ChatMessagesAPISendResponseModel

object ChatMessagesRepository {
    private val retrofitService: ChatMessagesAPIService by lazy {
        RetrofitClient.instance.create(ChatMessagesAPIService::class.java)
    }

    suspend fun getChatMessages(p1Id: String, p2Id: String) : ChatMessagesAPIGetResponseModel {
        return retrofitService.getChatMessages(p1Id, p2Id)
    }

    suspend fun sendChatMessages(chatMessagesAPISendRequestModel: ChatMessagesAPISendRequestModel)
    : ChatMessagesAPISendResponseModel {
        return retrofitService.sendChatMessage(chatMessagesAPISendRequestModel)
    }
}