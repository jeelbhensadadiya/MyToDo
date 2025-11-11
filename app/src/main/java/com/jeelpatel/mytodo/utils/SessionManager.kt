package com.jeelpatel.mytodo.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

class SessionManager @Inject constructor(context: Context) {

    private val pref: SharedPreferences =
        context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE)

    companion object {

    }

    fun saveUserSession(userId: Int) {
        pref.edit().apply {
            putInt(Config.KEY_USER_ID, userId)
            putBoolean(Config.KEY_IS_LOGGED_IN, true)
            apply()
        }
    }

    fun getUserId(): Int = pref.getInt(Config.KEY_USER_ID, 0)

    fun isLoggedIn(): Boolean = pref.getBoolean(Config.KEY_IS_LOGGED_IN, false)

    fun clearSession() {
        pref.edit().clear().apply()
    }
}
