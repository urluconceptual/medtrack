package com.unibuc.medtrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.ChatMessageDTO
import com.unibuc.medtrack.data.models.ChatMessageModel
import com.unibuc.medtrack.data.models.DoctorUserDTO
import com.unibuc.medtrack.data.models.PatientUserDTO
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepository
import com.unibuc.medtrack.data.repositories.doctor_patient.DoctorPatientRepository
import com.unibuc.medtrack.data.repositories.doctors.DoctorsRepository
import com.unibuc.medtrack.data.repositories.patients.PatientsRepository
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
    private val doctorPatientRepository: DoctorPatientRepository,
    private val doctorsRepository: DoctorsRepository,
    private val patientsRepository: PatientsRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _doctorDtos = MutableLiveData<List<DoctorUserDTO>>()
    val doctorDtos: LiveData<List<DoctorUserDTO>> get() = _doctorDtos

    private val _patientDtos = MutableLiveData<List<PatientUserDTO>>()
    val patientDtos: LiveData<List<PatientUserDTO>> get() = _patientDtos

    private val _myRole = MutableLiveData<UserType>()
    val myRole: LiveData<UserType> get() = _myRole

    val email = sessionManager.getUserEmail()

    fun loadDtos() {
        if (email != null) {
            viewModelScope.launch {
                val myId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id.toString()
                }

                _myRole.value = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.role
                }!!

                if (_myRole.value == UserType.PATIENT) {
                    val doctorIds = withContext(Dispatchers.IO) {
                        doctorPatientRepository.getDoctorIdsForPatient(myId)
                    }!!

                    _doctorDtos.value = withContext(Dispatchers.IO) {
                        doctorIds.map { doctorsRepository.getDoctorUserDtoById(it)!! }
                    }!!

                    _patientDtos.value = listOf()
                }
                else if (_myRole.value == UserType.DOCTOR) {
                    val patientIds = withContext(Dispatchers.IO) {
                        doctorPatientRepository.getPatientIdsForDoctor(myId)
                    }!!

                    _patientDtos.value = withContext(Dispatchers.IO) {
                        patientIds.map { patientsRepository.getPatientUserDtoById(it)!! }
                    }!!

                    _doctorDtos.value = listOf()
                }
            }
        }
    }
}