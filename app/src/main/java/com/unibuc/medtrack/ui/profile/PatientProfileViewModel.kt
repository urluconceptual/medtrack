package com.unibuc.medtrack.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.models.FullPatientProfile
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PatientProfileViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _patientProfile = MutableLiveData<FullPatientProfile?>()
    val patientProfile: LiveData<FullPatientProfile?> get() = _patientProfile

    fun loadPatientProfile(email: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                usersRepository.getFullPatientProfileByEmail(email)
            }
            Log.d("PatientProfileViewModel", "Loaded profile: $result")
            _patientProfile.value = result
        }
    }
}