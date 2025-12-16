package com.example.spineguard.ui.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spineguard.MainActivity
import com.example.spineguard.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Проверяем авторизацию
        if (auth.currentUser == null) {
            Toast.makeText(this, "Сначала войдите в систему", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        setupUI()
        loadUserData()
    }

    private fun setupUI() {
        // Кнопка сохранения
        binding.saveButton.setOnClickListener {
            saveProfile()
        }

        // Кнопка назад
        binding.backButton.setOnClickListener {
            finish()
        }

        // Email пользователя (не редактируется)
        binding.emailTextView.text = auth.currentUser?.email ?: ""
    }

    private fun loadUserData() {
        val userId = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Загружаем данные
                    val name = document.getString("name") ?: ""
                    val height = document.getLong("height")?.toInt()
                    val weight = document.getLong("weight")?.toInt()

                    binding.nameEditText.setText(name)
                    if (height != null) binding.heightEditText.setText(height.toString())
                    if (weight != null) binding.weightEditText.setText(weight.toString())
                } else {
                    // Создаем запись, если её нет
                    createUserDocument(userId)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Ошибка загрузки: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun createUserDocument(userId: String) {
        val email = auth.currentUser?.email ?: return

        val user = hashMapOf(
            "id" to userId,
            "email" to email,
            "name" to "",
            "height" to 0,
            "weight" to 0,
            "createdAt" to System.currentTimeMillis()
        )

        firestore.collection("users")
            .document(userId)
            .set(user)
            .addOnSuccessListener {
                Toast.makeText(this, "Создан новый профиль", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfile() {
        val userId = auth.currentUser?.uid ?: return
        val name = binding.nameEditText.text.toString().trim()
        val heightStr = binding.heightEditText.text.toString().trim()
        val weightStr = binding.weightEditText.text.toString().trim()

        // Валидация
        if (name.isEmpty()) {
            Toast.makeText(this, "Введите имя", Toast.LENGTH_SHORT).show()
            return
        }

        val height = if (heightStr.isNotEmpty()) heightStr.toIntOrNull() else null
        val weight = if (weightStr.isNotEmpty()) weightStr.toIntOrNull() else null

        // Проверка корректности числовых значений
        if (heightStr.isNotEmpty() && height == null) {
            Toast.makeText(this, "Введите корректный рост", Toast.LENGTH_SHORT).show()
            return
        }

        if (weightStr.isNotEmpty() && weight == null) {
            Toast.makeText(this, "Введите корректный вес", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = hashMapOf<String, Any>(
            "name" to name,
            "updatedAt" to System.currentTimeMillis()
        )

        // Добавляем рост и вес только если они не null
        height?.let { userData["height"] = it }
        weight?.let { userData["weight"] = it }

        firestore.collection("users")
            .document(userId)
            .update(userData)
            .addOnSuccessListener {
                Toast.makeText(this, "Профиль сохранен", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                // Если документа нет, создаем его
                if (e.message?.contains("No document to update") == true) {
                    createUserDocument(userId)
                    saveProfile() // Повторяем сохранение
                } else {
                    Toast.makeText(this, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}