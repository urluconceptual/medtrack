package com.unibuc.medtrack.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _role = MutableLiveData<UserType?>()
    val role: LiveData<UserType?> get() = _role

    suspend fun loadRole() : UserType? {
        val email = sessionManager.getUserEmail()
        return if (!email.isNullOrBlank()) {
            withContext(Dispatchers.IO) {
                usersRepository.getByEmail(email)?.role
            }
        } else null
    }
}
