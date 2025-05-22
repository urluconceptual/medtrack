package com.unibuc.medtrack.data.repositories.users

import com.unibuc.medtrack.data.models.UserModel

interface UsersRepository {
    suspend fun insert(users: UserModel)
    suspend fun getAll(): List<UserModel>
    suspend fun getByEmail(email: String): UserModel?
    suspend fun checkEmailAndPassword(email: String, password: String): Boolean
}