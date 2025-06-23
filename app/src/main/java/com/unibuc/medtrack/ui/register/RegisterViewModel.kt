package com.unibuc.medtrack.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.models.SignUpResponse
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.models.UserType
import com.unibuc.medtrack.data.models.DoctorModel
import com.unibuc.medtrack.data.models.PatientModel
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import com.unibuc.medtrack.data.repositories.doctors.DoctorsRepository
import com.unibuc.medtrack.data.repositories.patients.PatientsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val usersRepository: UsersRepository,
    private val doctorsRepository: DoctorsRepository,
    private val patientsRepository: PatientsRepository
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
            val id = UUID.randomUUID()
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
                val user = UserModel(id.toString(), email, name, password, accountType)
                usersRepository.insert(user)
                
                when (accountType) {
                    UserType.DOCTOR -> {
                        val doctor = DoctorModel(
                            id = UUID.randomUUID().toString(),
                            userId = id.toString(),
                            specialty = "General Medicine"
                        )
                        doctorsRepository.insert(doctor)
                    }
                    UserType.PATIENT -> {
                        val patient = PatientModel(
                            id = UUID.randomUUID().toString(),
                            userId = id.toString(),
                            dateOfBirth = "2002-05-23",
                            phoneNumber = "07722"
                        )
                        patientsRepository.insert(patient)
                    }
                }
            }
            _isSignUpSuccessful.value = SignUpResponse.SUCCESS
        }
    }
}