package com.unibuc.medtrack.data.repositories.users

import com.unibuc.medtrack.data.dao.UsersDAO
import com.unibuc.medtrack.data.models.UserModel

class UsersRepositoryLocal(val dao: UsersDAO): UsersRepository {
    override suspend fun insert(users: UserModel) = dao.insert(users)
    override suspend fun getAll(): List<UserModel> = dao.getAll()
    override suspend fun getByEmail(email: String): UserModel? = dao.getByEmail(email)
    override suspend fun checkEmailAndPassword(email: String, password: String): Boolean {
        return dao.checkEmailAndPassword(email, password) != null
    }
}