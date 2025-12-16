package com.example.spineguard.data

import android.content.Context

class AuthManager(context: Context) {

    private val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    fun register(email: String, password: String): Boolean {
        if (email.isBlank() || password.isBlank()) return false

        // очень простой вариант – храним одну пару логин/пароль
        prefs.edit()
            .putString("email", email)
            .putString("password", password)
            .apply()
        return true
    }

    fun login(email: String, password: String): Boolean {
        val savedEmail = prefs.getString("email", null)
        val savedPassword = prefs.getString("password", null)

        val success = (email == savedEmail && password == savedPassword)
        if (success) {
            prefs.edit().putBoolean("logged_in", true).apply()
        }
        return success
    }

    fun isLoggedIn(): Boolean =
        prefs.getBoolean("logged_in", false)

    fun logout() {
        prefs.edit().putBoolean("logged_in", false).apply()
    }
}
