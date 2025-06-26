package com.unibuc.medtrack.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.unibuc.medtrack.data.dao.ChatMessagesDAO
import androidx.room.TypeConverters
import com.unibuc.medtrack.data.dao.UsersDAO
import com.unibuc.medtrack.data.dao.DoctorsDAO
import com.unibuc.medtrack.data.dao.PatientsDAO
import com.unibuc.medtrack.data.models.ChatMessageModel
import com.unibuc.medtrack.data.dao.TreatmentsDAO
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.models.DoctorModel
import com.unibuc.medtrack.data.models.PatientModel
import com.unibuc.medtrack.data.models.TreatmentModel

@Database(
    entities = [
        UserModel::class,
        DoctorModel::class,
        PatientModel::class,
        ChatMessageModel::class
        PatientModel::class,
        TreatmentModel::class,
    ],
    version = 4
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val usersDao: UsersDAO
    abstract val doctorsDao: DoctorsDAO
    abstract val patientsDao: PatientsDAO
    abstract val chatMessageDao: ChatMessagesDAO
    abstract val treatmentsDAO: TreatmentsDAO
}