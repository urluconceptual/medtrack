package com.unibuc.medtrack.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.models.FullPatientProfile
import com.unibuc.medtrack.data.repositories.patients.PatientsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val patientsRepository: PatientsRepository
) : ViewModel() {

    private val _patientProfile = MutableLiveData<FullPatientProfile?>()
    val patientProfile: LiveData<FullPatientProfile?> get() = _patientProfile
    private val _saveSuccess = MutableLiveData<Boolean>()
    val saveSuccess: LiveData<Boolean> get() = _saveSuccess

    fun loadPatientProfile(email: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                usersRepository.getFullPatientProfileByEmail(email)
            }
            Log.d("PatientProfileViewModel", "Loaded profile: $result")
            _patientProfile.value = result
        }
    }

    fun savePatientProfile(profile: FullPatientProfile) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    usersRepository.updateUser(profile.user)
                    patientsRepository.updatePatient(profile.patient)
                }
                _saveSuccess.value = true
            }
    }

}