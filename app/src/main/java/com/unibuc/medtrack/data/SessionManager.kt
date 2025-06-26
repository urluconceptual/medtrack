package com.unibuc.medtrack.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUserEmail(email: String) {
        prefs.edit().putString("email", email).commit()
    }

    fun getUserEmail(): String? {
        return prefs.getString("email", null)
    }

    fun saveUserId(id: String) {
        prefs.edit().putString("id", id.toString()).commit()
    }

    fun getUserId(): String? {
        return prefs.getString("id", null)
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
