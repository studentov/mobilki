package com.example.spineguard.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.spineguard.R
import com.example.spineguard.util.ProgressManager
import com.example.spineguard.ui.progress.ProgressActivity
import com.example.spineguard.ui.timer.TimerActivity

class HomeFragment : Fragment(R.layout.fragment_home) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        val tvMotivation = view.findViewById<TextView>(R.id.tvMotivation)
        val tvTipText = view.findViewById<TextView>(R.id.tvTipText)
        val btnStartWorkout = view.findViewById<Button>(R.id.btnStartWorkout)

        val tvProgressText = view.findViewById<TextView>(R.id.tvProgressText)
        val btnShowProgress = view.findViewById<Button>(R.id.btnShowProgress)

        // Обновляем надпись прогресса
        val todayCount = ProgressManager.getTodayCount(requireContext())
        val goal = ProgressManager.getDailyGoal()
        tvProgressText.text = "Выполнено $todayCount/$goal сегодня"

        // Кнопка "Начать тренировку" -> окно таймера
        btnStartWorkout.setOnClickListener {
            val intent = Intent(requireContext(), TimerActivity::class.java)
            startActivity(intent)
        }

        // Кнопка "Статистика за 7 дней"
        btnShowProgress.setOnClickListener {
            val intent = Intent(requireContext(), ProgressActivity::class.java)
            startActivity(intent)
        }

        // Простое приветствие по времени суток
        val hour = java.util.Calendar.getInstance()
            .get(java.util.Calendar.HOUR_OF_DAY)

        val greeting = when (hour) {
            in 5..11 -> "Доброе утро!"
            in 12..17 -> "Добрый день!"
            in 18..22 -> "Добрый вечер!"
            else -> "Привет!"
        }

        tvGreeting.text = greeting
        tvMotivation.text = "Следи за спиной — начни тренировку или посмотри прогресс."
        tvTipText.text = "Старайся вставать и разминаться каждые 60 минут сидячей работы."
    }
}
