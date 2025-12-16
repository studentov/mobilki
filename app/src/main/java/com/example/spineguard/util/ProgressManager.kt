package com.example.spineguard.util

import android.content.Context
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ProgressManager {

    private const val PREFS = "spineguard_progress"
    private const val DAILY_GOAL = 10

    private val sdfKey = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val sdfLabel = SimpleDateFormat("dd.MM", Locale.getDefault())

    fun getDailyGoal(): Int = DAILY_GOAL

    fun incrementToday(context: Context) {
        val key = todayKey()
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val current = prefs.getInt(key, 0)
        prefs.edit().putInt(key, current + 1).apply() // apply() — не блокирует поток
    }

    fun getTodayCount(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return prefs.getInt(todayKey(), 0)
    }

    /** Возвращает список из 7 элементов: (метка_дня, количество) от сегодня назад */
    fun getLast7Days(context: Context): List<Pair<String, Int>> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val result = ArrayList<Pair<String, Int>>(7)

        val cal = Calendar.getInstance()
        for (i in 0..6) {
            val key = sdfKey.format(cal.time)
            val label = sdfLabel.format(cal.time)
            val count = prefs.getInt(key, 0)
            result.add(label to count)
            cal.add(Calendar.DAY_OF_YEAR, -1)
        }

        return result
    }

    fun resetAll(context: android.content.Context) {
        val prefs = context.getSharedPreferences(PREFS, android.content.Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }


    private fun todayKey(): String = sdfKey.format(Calendar.getInstance().time)
}
