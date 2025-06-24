package com.unibuc.medtrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.ChatMessageDTO
import com.unibuc.medtrack.data.models.ChatMessageModel
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PatientChatConversationViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val chatMessagesRepository: ChatMessagesRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _messageDtos = MutableLiveData<List<ChatMessageDTO>>()
    val messageDtos: LiveData<List<ChatMessageDTO>> get() = _messageDtos

    val email = sessionManager.getUserEmail()

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
            }
        }
    }
}