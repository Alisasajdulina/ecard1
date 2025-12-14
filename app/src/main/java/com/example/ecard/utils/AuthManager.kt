package com.example.ecard.utils

import android.content.Context
import android.content.SharedPreferences

class AuthManager(context: Context) {
    private val prefs: SharedPreferences = 
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
    
    private val KEY_USER_ID = "user_id"
    private val KEY_USER_EMAIL = "user_email"
    private val KEY_IS_LOGGED_IN = "is_logged_in"

    fun saveUserSession(userId: Long, email: String) {
        prefs.edit().apply {
            putLong(KEY_USER_ID, userId)
            putString(KEY_USER_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getCurrentUserId(): Long? {
        return if (isLoggedIn()) {
            prefs.getLong(KEY_USER_ID, -1).takeIf { it != -1L }
        } else {
            null
        }
    }

    fun getCurrentUserEmail(): String? {
        return if (isLoggedIn()) {
            prefs.getString(KEY_USER_EMAIL, null)
        } else {
            null
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}

