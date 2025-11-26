package com.example.sample.auth

import android.content.Context
import android.content.SharedPreferences

class TokenStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? {
        return prefs.getString("auth_token", null)
    }

    fun clear() {
        prefs.edit().remove("auth_token").apply()
    }
}
