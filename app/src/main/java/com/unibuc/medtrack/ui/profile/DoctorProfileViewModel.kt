package com.unibuc.medtrack.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibuc.medtrack.data.models.FullDoctorProfile
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class DoctorProfileViewModel @Inject constructor(
    private val usersRepository: UsersRepository
) : ViewModel() {

    private val _doctorProfile = MutableLiveData<FullDoctorProfile?>()
    val doctorProfile: LiveData<FullDoctorProfile?> get() = _doctorProfile

    fun loadDoctorProfile(email: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                usersRepository.getFullDoctorProfileByEmail(email)
            }
            Log.d("DoctorProfileVM", "Loaded profile: $result")
            _doctorProfile.value = result
        }
    }
}

