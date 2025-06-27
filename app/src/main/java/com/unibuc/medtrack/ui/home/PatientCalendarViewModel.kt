package com.unibuc.medtrack.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.repositories.notifications.NotificationsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PatientCalendarViewModel @Inject constructor(
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
    private val _selectedDate = MutableLiveData<LocalDateTime>()
    val selectedDate: LiveData<LocalDateTime> get() = _selectedDate

    fun fetchFullTreatmentDetails(date: LocalDateTime) {
        val patientId = sessionManager.getUserId() ?: return

        viewModelScope.launch {
            try {
                val result = notificationsRepository.getTodayNotificationsWithTreatment(patientId, date)
                Log.i("PatientHomeViewModel", "result: " + result)
                Log.i("PatientHomeViewModel", "all notif: " + notificationsRepository.getAll())
                _todaysNotifications.value = result
            } catch (e: Exception) {
                Log.e("PatientCalendarVM", "Error fetching full treatments", e)
                _error.value = "Failed to fetch notifications: ${e.message}"
            }
        }
    }

    fun updateSelectedDate(localDate: LocalDateTime) {
        _selectedDate.value = localDate
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

    fun fetchTodayNotifications() {
        if (_selectedDate.value == null)
            return

        val patientId = sessionManager.getUserId() ?: return

        viewModelScope.launch {
            try {
                val result = notificationsRepository.getTodayNotificationsWithTreatment(patientId, _selectedDate.value!!)
                Log.i("PatientHomeViewModel", "result: " + result)
                Log.i("PatientHomeViewModel", "all notif: " + notificationsRepository.getAll())
                _todaysNotifications.value = result
            } catch (e: Exception) {
                Log.e("PatientCalendarVM", "Error fetching full treatments", e)
                _error.value = "Failed to fetch notifications: ${e.message}"
            }
        }
    }
}