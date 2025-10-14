package com.jeelpatel.mytodo.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_USER_ID = "uId"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
    }

    fun saveUserSession(userId: Int) {
        pref.edit().apply {
            putInt(KEY_USER_ID, userId)
            putBoolean(KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): Int = pref.getInt(KEY_USER_ID, 0)

    fun isLoggedIn(): Boolean = pref.getBoolean(KEY_IS_LOGGED_IN, false)

    fun clearSession() {
        pref.edit().clear().apply()
    }
}
