package com.unibuc.medtrack.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.SessionManager
import com.unibuc.medtrack.data.models.LoginResponse
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    private val _isLoginSuccessful = MutableLiveData<LoginResponse>()
    val isLoginSuccessful: LiveData<LoginResponse> get() = _isLoginSuccessful

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
            _isLoginSuccessful.value = LoginResponse.SUCCESS
            sessionManager.saveUserEmail(email)

        }
    }
}