package com.example.spineguard.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spineguard.R
import com.example.spineguard.ui.auth.LoginActivity
import com.example.spineguard.util.ProgressManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tvProfileName)
        val tvEmail = view.findViewById<TextView>(R.id.tvProfileEmail)
        val tvInfo = view.findViewById<TextView>(R.id.tvProfileInfo)

        val btnEditName = view.findViewById<Button>(R.id.btnEditName)
        val btnResetProgress = view.findViewById<Button>(R.id.btnResetProgress)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val progress = view.findViewById<ProgressBar>(R.id.profileLoading)

        val user = auth.currentUser
        if (user == null) {
            goToLogin()
            return
        }

        tvEmail.text = user.email ?: "—"

        // прогресс
        val today = ProgressManager.getTodayCount(requireContext())
        val goal = ProgressManager.getDailyGoal()
        tvInfo.text = "Сегодня выполнено: $today/$goal"

        // загрузка имени из Firestore
        progress.visibility = View.VISIBLE
        val uid = user.uid

        db.collection("users").document(uid).get()
            .addOnSuccessListener { doc ->
                val name = doc.getString("name")
                tvName.text = if (!name.isNullOrBlank()) name else "Пользователь"
                progress.visibility = View.GONE
            }
            .addOnFailureListener {
                tvName.text = "Пользователь"
                progress.visibility = View.GONE
            }

        // 2) Изменить имя
        btnEditName.setOnClickListener {
            showEditNameDialog(
                currentName = tvName.text?.toString().orEmpty(),
                uid = uid,
                onSaved = { newName ->
                    tvName.text = newName
                }
            )
        }

        btnResetProgress.setOnClickListener {
            ProgressManager.resetAll(requireContext())
            Toast.makeText(requireContext(), "Прогресс сброшен", Toast.LENGTH_SHORT).show()

            val newToday = ProgressManager.getTodayCount(requireContext())
            tvInfo.text = "Сегодня выполнено: $newToday/$goal"
        }

        btnLogout.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Вы вышли из аккаунта", Toast.LENGTH_SHORT).show()
            goToLogin()
        }
    }

    private fun showEditNameDialog(currentName: String, uid: String, onSaved: (String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_edit_name, null)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etName)

        // аккуратно проставим текущее имя (если оно "Пользователь" — оставим пусто)
        etName.setText(if (currentName == "Пользователь") "" else currentName)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Изменить имя")
            .setView(dialogView)
            .setPositiveButton("Сохранить") { _, _ ->
                val newName = etName.text?.toString()?.trim().orEmpty()
                if (newName.isBlank()) {
                    Toast.makeText(requireContext(), "Имя не может быть пустым", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // сохраняем в Firestore users/{uid}.name
                db.collection("users").document(uid)
                    .set(mapOf("name" to newName), com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "Имя сохранено", Toast.LENGTH_SHORT).show()
                        onSaved(newName)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(requireContext(), "Ошибка: ${e.message}", Toast.LENGTH_LONG).show()
                    }
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun goToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }
}
