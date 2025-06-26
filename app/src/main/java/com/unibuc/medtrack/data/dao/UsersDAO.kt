package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unibuc.medtrack.data.models.UserModel

@Dao
interface UsersDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: UserModel)

    @Query("""
        SELECT *
        FROM users
        WHERE id = :id
    """)
    suspend fun getById(id: String): UserModel?

    @Query("""
        SELECT *
        FROM users
    """)
    suspend fun getAll(): List<UserModel>

    @Query("""
        SELECT *
        FROM users
        WHERE email = :email
    """)
    suspend fun getByEmail(email: String): UserModel?

    @Query("""
        SELECT *
        FROM users
        WHERE email = :email AND password = :password
    """)
    suspend fun checkEmailAndPassword(email: String, password: String): UserModel?

    @Update
    suspend fun update(user: UserModel)
}