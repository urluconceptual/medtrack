package com.unibuc.medtrack.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.models.SignUpResponse
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
class RegisterViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {
    private val _isSignUpSuccessful = MutableLiveData<SignUpResponse>()
    val isSignUpSuccessful: LiveData<SignUpResponse> get() = _isSignUpSuccessful

    public fun submitSignUpForm(
        name: String?,
        email: String?,
        password: String?,
        accountType: UserType?
    ) {
        viewModelScope.launch {
            val id = UUID.randomUUID().toString()
            if (name.isNullOrBlank() || email.isNullOrBlank() || password.isNullOrBlank() || accountType == null) {
                _isSignUpSuccessful.value = SignUpResponse.EMPTY_FIELDS
                return@launch
            }
            val isEmailUsed = withContext(Dispatchers.IO) {
                usersRepository.getByEmail(email) != null
            }
            if (isEmailUsed) {
                _isSignUpSuccessful.value = SignUpResponse.EMAIL_TAKEN
                return@launch
            }
            withContext(Dispatchers.IO) {
                usersRepository.insert(UserModel(id, name, email, password, accountType))
            }
            _isSignUpSuccessful.value = SignUpResponse.SUCCESS
        }
    }
}