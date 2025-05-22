package com.unibuc.medtrack.data.repositories.users

import com.unibuc.medtrack.data.models.UserModel

interface UsersRepository {
    suspend fun insert(users: List<UserModel>)
    suspend fun getAll(): List<UserModel>
}