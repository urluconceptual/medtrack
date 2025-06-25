package com.unibuc.medtrack.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.TreatmentModel
import com.unibuc.medtrack.data.repositories.treatments.TreatmentsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PatientHomeViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val sessionManager: SessionManager,
    private val treatmentsRepository: TreatmentsRepository
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName
    val email = sessionManager.getUserEmail()
    private val _treatments = MutableLiveData<List<TreatmentModel>>()
    val treatments: LiveData<List<TreatmentModel>> = _treatments
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


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

    fun fetchFullTreatmentDetails() {
        val patientId = sessionManager.getUserId()
        if (patientId.isNullOrBlank()) {
            _error.value = "Patient ID not found"
            return
        }

        viewModelScope.launch {
            try {
                val data = treatmentsRepository.getByPatientId(patientId)
                Log.i("PatientCalendarVM", "Full treatments: $data")
                _treatments.value = data
            } catch (e: Exception) {
                Log.e("PatientCalendarVM", "Error fetching full treatments", e)
                _error.value = "Failed to load full treatments: ${e.message ?: "Unknown error"}"
            }
        }
    }
}