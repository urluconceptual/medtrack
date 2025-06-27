package com.unibuc.medtrack.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.LoginResponse
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _isLoginSuccessful = MutableLiveData<LoginResponse>()
    val isLoginSuccessful: LiveData<LoginResponse> get() = _isLoginSuccessful

    private val _role = MutableLiveData<UserType?>()
    val role: LiveData<UserType?> get() = _role
    val email = sessionManager.getUserEmail()

    fun loadRole(email: String) {
        viewModelScope.launch {
            val role = withContext(Dispatchers.IO) {
                usersRepository.getByEmail(email)?.role
            }
            _role.value = role
        }
    }

    public fun submitLoginForm(
        email: String?,
        password: String?,
    ) {
        viewModelScope.launch {
            if (email.isNullOrBlank() || password.isNullOrBlank()) {
                _isLoginSuccessful.value = LoginResponse.EMPTY_FIELDS
                return@launch
            }
            val areCredentialsCorrect = withContext(Dispatchers.IO) {
                usersRepository.checkEmailAndPassword(email, password)
            }
            if (!areCredentialsCorrect) {
                _isLoginSuccessful.value = LoginResponse.WRONG_CREDENTIALS
                return@launch
            }
            sessionManager.saveUserEmail(email)
            viewModelScope.launch {
                try {
                    val user = withContext(Dispatchers.IO) {
                        usersRepository.getByEmail(email)
                    }

                    user?.let {
                        sessionManager.saveUserId(it.id)
                    } ?: run {
                        // Handle case where user is null
                        Log.e("ViewModel", "User not found for email: $email")
                    }
                } catch (e: Exception) {
                    Log.e("ViewModel", "Failed to fetch user by email", e)
                }
            }


            _isLoginSuccessful.value = LoginResponse.SUCCESS
        }
    }
}