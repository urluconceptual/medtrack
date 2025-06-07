package com.unibuc.medtrack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unibuc.medtrack.data.dao.UsersDAO
import com.unibuc.medtrack.data.dao.DoctorsDAO
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.models.DoctorModel

@Database(
    entities = [
        UserModel::class,
        DoctorModel::class
    ],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract val usersDao: UsersDAO
    abstract val doctorsDao: DoctorsDAO
}