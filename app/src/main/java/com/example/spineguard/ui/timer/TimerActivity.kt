package com.example.spineguard.ui.timer

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.spineguard.R
import com.example.spineguard.util.ProgressManager

class TimerActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private var isRunning = false
    private val totalMillis = 60_000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        val tvTimer = findViewById<TextView>(R.id.tvTimer)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnExit = findViewById<Button>(R.id.btnExit)

        fun updateTimerText(millis: Long) {
            val seconds = millis / 1000
            val min = seconds / 60
            val sec = seconds % 60
            tvTimer.text = String.format("%02d:%02d", min, sec)
        }

        btnStart.setOnClickListener {
            if (isRunning) return@setOnClickListener

            isRunning = true
            timer?.cancel()
            timer = object : CountDownTimer(totalMillis, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    updateTimerText(millisUntilFinished)
                }

                override fun onFinish() {
                    isRunning = false
                    updateTimerText(0)
                    ProgressManager.incrementToday(this@TimerActivity)
                    Toast.makeText(this@TimerActivity, "Готово! Проверка засчитана 👍", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }

        btnCancel.setOnClickListener {
            timer?.cancel()
            isRunning = false
            updateTimerText(totalMillis)
            Toast.makeText(this, "Таймер сброшен", Toast.LENGTH_SHORT).show()
        }

        btnExit.setOnClickListener {
            timer?.cancel()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
