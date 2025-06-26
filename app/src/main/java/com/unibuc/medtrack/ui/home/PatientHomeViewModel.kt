package com.unibuc.medtrack.ui.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.NotificationModel
import com.unibuc.medtrack.data.models.TreatmentModel
import com.unibuc.medtrack.data.repositories.notifications.NotificationsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import com.unibuc.medtrack.data.worker.NotificationWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class PatientHomeViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val sessionManager: SessionManager,
    private val notificationsRepository: NotificationsRepository
) : ViewModel() {

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> get() = _userName
    val email = sessionManager.getUserEmail()
    private val _todaysNotifications = MutableLiveData<List<FullTreatmentWithNotifications>>()
    val todaysNotifications: LiveData<List<FullTreatmentWithNotifications>> = _todaysNotifications
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

    fun fetchTodayNotifications() {
        val patientId = sessionManager.getUserId() ?: return

        viewModelScope.launch {
            try {
                val today = LocalDateTime.now()
                val result = notificationsRepository.getTodayNotificationsWithTreatment(patientId, today)
                Log.i("PatientHomeViewModel", "result: " + result)
                Log.i("PatientHomeViewModel", "all notif: " + notificationsRepository.getAll())
                _todaysNotifications.value = result
            } catch (e: Exception) {
                Log.e("PatientCalendarVM", "Error fetching full treatments", e)
                _error.value = "Failed to fetch notifications: ${e.message}"
            }
        }
    }

    fun scheduleNotification(context: Context, notification: NotificationModel, treatment: TreatmentModel) {
        Log.d("PatientHomeViewModel", "Entered schedule notification")

        val delay = Duration.between(LocalDateTime.now(), notification.time).toMillis()

        if (delay <= 0) {
            Log.w("PatientHomeViewModel", "Skipped past notification at ${notification.time}")
            return
        }

        val data = Data.Builder()
            .putString("treatmentName", treatment.medicineName)
            .putString("dosage", "${treatment.dosage} ${treatment.dosageUnit}")
            .build()

        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        WorkManager.getInstance(context).enqueue(workRequest)
        Log.d("PatientHomeViewModel", "Scheduled notification for ${notification.time}")
    }

    fun scheduleAllTodayNotifications(context: Context) {
        Log.d("PatientHomeViewModel", "Entered schedule all today notifications")

        val patientId = sessionManager.getUserId() ?: return

        viewModelScope.launch {
            try {
                val today = LocalDateTime.now()
                val notificationsWithTreatments =
                    notificationsRepository.getTodayNotificationsWithTreatment(patientId, today)

                notificationsWithTreatments.forEach {
                    scheduleNotification(context, it.notification, it.treatment)
                }

                Log.d("PatientHomeViewModel", "Scheduled ${notificationsWithTreatments.size} notifications.")
            } catch (e: Exception) {
                Log.e("PatientHomeViewModel", "Error scheduling today's notifications", e)
            }
        }
    }

    fun markNotificationAsTaken(notificationId: String) {
        Log.i("PatientHomeViewModel", "Entered mark notification as taken: " + notificationId)
        viewModelScope.launch {
            try {
                notificationsRepository.updateTakenAt(notificationId, LocalDateTime.now())
                fetchTodayNotifications()
            } catch (e: Exception) {
                Log.e("PatientHomeViewModel", "Error updating takenAt", e)
                _error.value = "Failed to update notification: ${e.message}"
            }
        }
    }

}
