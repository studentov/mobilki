package com.example.spineguard.ui.progress

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spineguard.R

class ProgressFragment : Fragment(R.layout.fragment_progress) {

    private lateinit var todayCountText: TextView
    private lateinit var todayStatusText: TextView
    private lateinit var resetButton: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        todayCountText = view.findViewById(R.id.todayCountText)
        todayStatusText = view.findViewById(R.id.todayStatusText)
        resetButton = view.findViewById(R.id.resetButton)

        updateUi()

        resetButton.setOnClickListener {
            resetToday()
            updateUi()
        }
    }

    private fun getTodayChecks(): Int {
        val prefs = requireContext().getSharedPreferences("posture_prefs", Context.MODE_PRIVATE)
        return prefs.getInt("today_checks", 0)
    }

    private fun resetToday() {
        val prefs = requireContext().getSharedPreferences("posture_prefs", Context.MODE_PRIVATE)
        prefs.edit().putInt("today_checks", 0).apply()
    }

    private fun updateUi() {
        val count = getTodayChecks()
        todayCountText.text = "Сегодняшние проверки: $count/8"

        todayStatusText.text = when {
            count == 0 -> "Начни с первой проверки осанки!"
            count in 1..4 -> "Уже неплохо, продолжай в том же духе!"
            count in 5..7 -> "Почти норма за день, ещё чуть-чуть!"
            else -> "Отлично! Ты выполнил дневную норму 🎉"
        }
    }
}
