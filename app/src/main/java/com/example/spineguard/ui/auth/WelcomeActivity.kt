// auth/WelcomeActivity.kt
package com.example.spineguard.ui.auth

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.spineguard.MainActivity
import com.example.spineguard.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class WelcomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWelcomeBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        // Задержка для показа splash screen (2 секунды)
        Handler(Looper.getMainLooper()).postDelayed({
            checkAuthStatus()
        }, 2000)
    }

    private fun checkAuthStatus() {
        val user = auth.currentUser

        if (user != null) {
            // Пользователь уже вошел - идем на MainActivity
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            // Пользователь не вошел - идем на LoginActivity
            startActivity(Intent(this, LoginActivity::class.java))
        }
        finish()
    }
}