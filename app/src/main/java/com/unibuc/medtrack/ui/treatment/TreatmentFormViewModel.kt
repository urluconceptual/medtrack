package com.unibuc.medtrack.ui.treatment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.NotificationModel
import com.unibuc.medtrack.data.models.TreatmentModel
import com.unibuc.medtrack.data.repositories.notifications.NotificationsRepository
import com.unibuc.medtrack.data.repositories.treatments.TreatmentsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TreatmentFormViewModel @Inject constructor(
    private val repository: TreatmentsRepository,
    private val notificationsRepository: NotificationsRepository,
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
                    insertNotificationsForTreatment(treatment)
                }
            }
        }
    }

    suspend fun insertNotificationsForTreatment(
        treatment: TreatmentModel
    ) {
        val notifications = mutableListOf<NotificationModel>()
        var current = parseDateTime(treatment.startDate).truncatedTo(ChronoUnit.MINUTES)
        val endTruncated = parseDateTime(treatment.endDate).truncatedTo(ChronoUnit.MINUTES)

        while (!current.isAfter(endTruncated)) {
            notifications.add(
                NotificationModel(
                    id = UUID.randomUUID().toString(),
                    treatmentId = treatment.id,
                    time = current,
                    takenAt = null
                )
            )
            current = current.plusHours(treatment.dose_interval.toLong())
        }
        notificationsRepository.insertAll(notifications)
    }

    fun parseDateTime(dateTimeStr: String): LocalDateTime {
        return LocalDate.parse(dateTimeStr).atStartOfDay()
    }
}