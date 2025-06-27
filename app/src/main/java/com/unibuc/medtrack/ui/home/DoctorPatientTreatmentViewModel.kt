package com.unibuc.medtrack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.models.TreatmentModel
import com.unibuc.medtrack.data.repositories.treatments.TreatmentsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DoctorPatientTreatmentViewModel @Inject constructor(
    private val treatmentRepository: TreatmentsRepository,
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _treatments = MutableLiveData<List<TreatmentModel>>()
    val treatments: LiveData<List<TreatmentModel>> = _treatments
    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> = _username

    fun loadTreatmentsForPatient(patientId: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                treatmentRepository.getByPatientId(patientId)
            }
            _treatments.value = result
        }
    }

    fun loadUsernameForPatient(patientId: String) {
        viewModelScope.launch {
            val name = withContext(Dispatchers.IO) {
                usersRepository.getById(patientId)?.name
            }
            _username.value = name
        }
    }
}
