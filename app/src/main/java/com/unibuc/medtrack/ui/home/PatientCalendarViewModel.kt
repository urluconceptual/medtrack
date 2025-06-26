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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Calendar
import javax.inject.Inject
import kotlin.math.max

@HiltViewModel
class PatientCalendarViewModel @Inject constructor(
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

    fun fetchFullTreatmentDetails(date: Calendar) {
        val patientId = sessionManager.getUserId()
        if (patientId.isNullOrBlank()) {
            _error.value = "Patient ID not found"
            return
        }

        viewModelScope.launch {
            try {
                val data = treatmentsRepository.getByPatientId(patientId)

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val targetDate = LocalDate.of(
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH) + 1,
                    date.get(Calendar.DAY_OF_MONTH)
                )

                val filteredData = data.filter { treatment ->
                    val startDate = LocalDate.parse(treatment.startDate, formatter)
                    val endDate = LocalDate.parse(treatment.endDate, formatter)

                    if (targetDate.isBefore(startDate) || targetDate.isAfter(endDate)) {
                        return@filter false
                    }

                    val daysBetween = ChronoUnit.DAYS.between(startDate, targetDate)
                    val doseIntervalDays = max(treatment.dose_interval / 24, 1)

                    (daysBetween % doseIntervalDays == 0L)
                }

                Log.i("PatientCalendarVM", "Treatments on this day: $filteredData")
                _treatments.value = filteredData
            } catch (e: Exception) {
                Log.e("PatientCalendarVM", "Error fetching full treatments", e)
                _error.value = "Failed to load full treatments: ${e.message ?: "Unknown error"}"
            }
        }
    }
}