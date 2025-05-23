package com.unibuc.medtrack

import android.app.Application
import androidx.room.Room
import com.unibuc.medtrack.data.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationController : Application() {
    private lateinit var appDatabase: AppDatabase

    companion object {
        var instance: ApplicationController? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    private fun initDatabase() {
        appDatabase = Room.databaseBuilder(
            context = this,
            klass = AppDatabase::class.java,
            name = "local_db",
        ).fallbackToDestructiveMigration().build()
    }
}