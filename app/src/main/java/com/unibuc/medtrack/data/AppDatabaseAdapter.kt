package com.unibuc.medtrack.data

import android.content.Context
import androidx.room.Room
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepositoryLocal
import com.unibuc.medtrack.data.repositories.doctors.DoctorsRepository
import com.unibuc.medtrack.data.repositories.doctors.DoctorsRepositoryLocal
import com.unibuc.medtrack.data.repositories.patients.PatientsRepository
import com.unibuc.medtrack.data.repositories.patients.PatientsRepositoryLocal
import com.unibuc.medtrack.data.repositories.chat_messages.ChatMessagesRepositoryLocal
import com.unibuc.medtrack.data.repositories.notifications.NotificationsRepository
import com.unibuc.medtrack.data.repositories.notifications.NotificationsRepositoryLocal
import com.unibuc.medtrack.data.repositories.treatments.TreatmentRepositoryLocal
import com.unibuc.medtrack.data.repositories.treatments.TreatmentsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppDatabaseAdapter {
    @Provides
    @Singleton
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = AppDatabase::class.java,
            name = "local_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideUsersRepository(appDatabase: AppDatabase): UsersRepository = UsersRepositoryLocal(appDatabase.usersDao, appDatabase.doctorsDao, appDatabase.patientsDao)

    @Provides
    @Singleton
    fun provideDoctorsRepository(appDatabase: AppDatabase): DoctorsRepository = DoctorsRepositoryLocal(appDatabase.doctorsDao)

    @Provides
    @Singleton
    fun providePatientsRepository(appDatabase: AppDatabase): PatientsRepository = PatientsRepositoryLocal(appDatabase.patientsDao)

    @Provides
    @Singleton
    fun provideChatMessagesRepository(appDatabase: AppDatabase): ChatMessagesRepository = ChatMessagesRepositoryLocal(appDatabase.chatMessageDao)

    @Provides
    @Singleton
    fun provideTreatmentsRepository(appDatabase: AppDatabase): TreatmentsRepository = TreatmentRepositoryLocal(appDatabase.treatmentsDAO)

    @Provides
    @Singleton
    fun provideNotificationsRepository(appDatabase: AppDatabase): NotificationsRepository = NotificationsRepositoryLocal(appDatabase.notificationsDAO)
}