package com.unibuc.medtrack.data.repositories.users

import com.unibuc.medtrack.data.models.FullDoctorProfile
import com.unibuc.medtrack.data.models.FullPatientProfile
import com.unibuc.medtrack.data.models.UserModel

interface UsersRepository {
    suspend fun insert(users: UserModel)
    suspend fun getAll(): List<UserModel>
    suspend fun getByEmail(email: String): UserModel?
    suspend fun checkEmailAndPassword(email: String, password: String): Boolean
    suspend fun getFullDoctorProfileByEmail(email: String): FullDoctorProfile?
    suspend fun getFullPatientProfileByEmail(email: String): FullPatientProfile?
    suspend fun updateUser(user: UserModel)
}