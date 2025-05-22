package com.unibuc.medtrack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unibuc.medtrack.data.dao.UsersDAO
import com.unibuc.medtrack.data.models.UserModel

@Database(
    entities = [
        UserModel::class
    ],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract val usersDao: UsersDAO
}