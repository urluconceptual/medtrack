package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibuc.medtrack.data.models.UserModel

@Dao
interface UsersDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(users: List<UserModel>)

    @Query("""
        SELECT *
        FROM users
    """)
    suspend fun getAll(): List<UserModel>
}