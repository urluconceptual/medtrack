package com.unibuc.medtrack.data

import android.content.Context
import androidx.room.Room
import com.unibuc.medtrack.data.repositories.users.UsersRepository
import com.unibuc.medtrack.data.repositories.users.UsersRepositoryLocal
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
    fun provideUsersRepository(appDatabase: AppDatabase): UsersRepository = UsersRepositoryLocal(appDatabase.usersDao)
}