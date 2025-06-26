package com.unibuc.medtrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.repositories.doctor_patient.DoctorPatientRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DoctorHomeViewModel @Inject constructor(
    private val doctorPatientRepo: DoctorPatientRepository,
    public val usersRepository: UsersRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    val patients = MutableLiveData<List<UserModel>>()

    private val _userName = MutableLiveData<String>()
    private val _role = MutableLiveData<UserType?>()
    val userName: LiveData<String> get() = _userName
    val role: LiveData<UserType?> get() = _role
    val email = sessionManager.getUserEmail()


    fun loadUserName() {
        if (email != null) {
            viewModelScope.launch {
                val name = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.name ?: ""
                }
                _userName.value = name
            }
        }
    }

    fun loadRole() {
        if (email != null) {
            viewModelScope.launch {
                val role = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.role
                }
                _role.value = role
            }
        }
    }

    fun loadPatients() {
        if (email != null) {
            viewModelScope.launch {
                val id = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id ?: ""
                }

                val ids = doctorPatientRepo.getPatientIdsForDoctor(id)
                val users = ids.mapNotNull { usersRepository.getById(it) }
                patients.postValue(users)
            }
        }
    }

    fun addPatient(patientId: String) {
        if (email != null) {
            viewModelScope.launch {
                val doctorId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id ?: ""
                }

                doctorPatientRepo.addDoctorPatient(doctorId, patientId)
                loadPatients()
            }
        }
    }
}
