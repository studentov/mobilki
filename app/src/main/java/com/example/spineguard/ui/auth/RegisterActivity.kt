package com.example.spineguard.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spineguard.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.spineguard.MainActivity


class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setupClickListeners()
    }

    private fun setupClickListeners() {
        Log.d("RegisterActivity", "Настройка слушателей")

        binding.registerButton.setOnClickListener {
            Log.d("RegisterActivity", "Нажата кнопка регистрации")
            registerUser()
        }

        binding.loginTextView.setOnClickListener {
            Log.d("RegisterActivity", "Нажата ссылка на вход")
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val name = binding.nameEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Введите корректный email", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Пароль должен быть не менее 6 символов", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("RegisterActivity", "Начинаем регистрацию: email=$email, name=$name")
        binding.progressBar.visibility = android.view.View.VISIBLE

        // Регистрация в Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("RegisterActivity", "Firebase Auth успешно: userId=${auth.currentUser?.uid}")
                    // Сохранение данных пользователя в Firestore
                    val userId = auth.currentUser?.uid ?: ""
                    saveUserToFirestore(userId, email, name)
                } else {
                    binding.progressBar.visibility = android.view.View.GONE
                    Log.e("RegisterActivity", "Ошибка Firebase Auth: ${task.exception}")
                    Toast.makeText(
                        this,
                        "Ошибка регистрации: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = android.view.View.GONE
                Log.e("RegisterActivity", "Ошибка в addOnFailureListener: $e")
                Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


    private fun saveUserToFirestore(userId: String, email: String, name: String) {
        val user = hashMapOf(
            "id" to userId,
            "email" to email,
            "name" to name,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(userId)
            .set(user)
            .addOnCompleteListener { task ->
                binding.progressBar.visibility = android.view.View.GONE

                if (task.isSuccessful) {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                    // Переход на главный экран
                    startActivity(Intent(this, com.example.spineguard.MainActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Ошибка сохранения данных: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}