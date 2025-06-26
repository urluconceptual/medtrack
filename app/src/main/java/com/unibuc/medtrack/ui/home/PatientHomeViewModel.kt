package com.unibuc.medtrack.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.repositories.notifications.NotificationsRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
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
}