package com.unibuc.medtrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.ChatMessageDTO
import com.unibuc.medtrack.data.models.ChatMessageModel
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepository as ChatMessagesDataRepository
import com.unibuc.medtrack.networking.repositories.ChatMessagesRepository as ChatMessagesAPIRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.time.LocalDate
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class PatientChatConversationViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val chatMessagesRepository: ChatMessagesDataRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _messageDtos = MutableLiveData<List<ChatMessageDTO>>()
    val messageDtos: LiveData<List<ChatMessageDTO>> get() = _messageDtos

    private val _myRole = MutableLiveData<UserType>()
    val myRole: LiveData<UserType> get() = _myRole

    private val _otherId = MutableLiveData<String>()
    val otherId: LiveData<String> get() = _otherId

    val email = sessionManager.getUserEmail()

    fun loadRole() {
        if (email != null) {
            viewModelScope.launch {
                _myRole.value = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.role
                }!!
            }
        }
    }
    fun loadMessageDtos(otherId: String) {
        if (email != null) {
            viewModelScope.launch {
                val myId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id.toString()
                }

                val messages = withContext(Dispatchers.IO) {
                    chatMessagesRepository.getAllByParticipants(myId, otherId)
                }

                _messageDtos.value = messages.map {
                    ChatMessageDTO.fromModel(it, myId)
                }

                _otherId.value = otherId
            }
        }
    }

    fun sendChatMessage(message: String) {
        if (email != null) {
            viewModelScope.launch {
                val myId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id.toString()
                }

                val now = LocalDate.now().toString()
                val chatMessage = ChatMessageModel(
                    id = UUID.randomUUID().toString(),
                    senderId = myId,
                    receiverId = _otherId.value!!,
                    message = message,
                    timeSent = now)

                chatMessagesRepository.insert(chatMessage)

                loadMessageDtos(_otherId.value!!)
            }
        }
    }

    fun getChatMessages(otherId: String) {
        if (email != null) {
            viewModelScope.launch {
                val myId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id.toString()
                }

                _otherId.value = otherId
                val receiverId = _otherId.value ?: return@launch

                try {
                    val response = withContext(Dispatchers.IO) {
                        ChatMessagesAPIRepository.getChatMessages(myId, receiverId)
                    }

                    val chatMessages = response.data
                    withContext(Dispatchers.IO) {
                        chatMessages.forEach { message ->
                            chatMessagesRepository.insert(message)
                        }
                    }

                    loadMessageDtos(receiverId)

                } catch (e: IOException) {}
            }
        }
    }
}