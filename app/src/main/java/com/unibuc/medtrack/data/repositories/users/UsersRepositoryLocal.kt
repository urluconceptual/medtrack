package com.unibuc.medtrack.data.repositories.users

import com.unibuc.medtrack.data.dao.UsersDAO
import com.unibuc.medtrack.data.models.UserModel

class UsersRepositoryLocal(val dao: UsersDAO): UsersRepository {
    override suspend fun insert(users: List<UserModel>) = dao.insert(users)
    override suspend fun getAll(): List<UserModel> = dao.getAll()
}