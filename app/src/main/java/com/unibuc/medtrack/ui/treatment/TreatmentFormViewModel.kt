package com.unibuc.medtrack.ui.treatment

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
class TreatmentFormViewModel @Inject constructor(
    private val repository: TreatmentsRepository,
    private val usersRepository: UsersRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    val email = sessionManager.getUserEmail()

    fun saveTreatment(treatment: TreatmentModel) {
        if (email !== null) {
            viewModelScope.launch {

                val doctorId = withContext(Dispatchers.IO) {
                    usersRepository.getByEmail(email)?.id ?: ""
                }
                treatment.doctorId = doctorId
                viewModelScope.launch {
                    repository.insert(treatment)
                }
            }
        }
    }
}