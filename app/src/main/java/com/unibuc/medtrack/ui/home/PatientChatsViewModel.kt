package com.unibuc.medtrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.ChatMessageDTO
import com.unibuc.medtrack.data.models.ChatMessageModel
import com.unibuc.medtrack.data.models.DoctorUserDTO
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepository
import com.unibuc.medtrack.data.repositories.doctors.DoctorsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PatientChatsViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val chatMessagesRepository: ChatMessagesRepository,
    private val doctorsRepository: DoctorsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _doctorDtos = MutableLiveData<List<DoctorUserDTO>>()
    val doctorDtos: LiveData<List<DoctorUserDTO>> get() = _doctorDtos

    val email = sessionManager.getUserEmail()

    fun loadDoctorDtos() {
        if (email != null) {
            viewModelScope.launch {
                val myId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id.toString()
                }

                val doctorIds = withContext(Dispatchers.IO) {
                    chatMessagesRepository.getAllMyConversationUserIds(myId)
                }!!

                _doctorDtos.value = withContext(Dispatchers.IO) {
                    doctorIds.map { doctorsRepository.getDoctorUserDtoById(it) !! }
                }!!
            }
        }
    }
}