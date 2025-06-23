package com.unibuc.medtrack.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getUserEmail(): String? {
        return prefs.getString("email", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
