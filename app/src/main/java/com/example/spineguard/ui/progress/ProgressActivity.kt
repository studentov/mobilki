package com.example.spineguard.ui.progress

import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.spineguard.R
import com.example.spineguard.util.ProgressManager

class ProgressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_progress)

        val container = findViewById<LinearLayout>(R.id.containerStats)

        val stats = ProgressManager.getLast7Days(this)

        val lp = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            bottomMargin = 8
        }

        for ((dateLabel, count) in stats) {
            val tv = TextView(this).apply {
                layoutParams = lp
                text = "$dateLabel — $count раз(а)"
                textSize = 16f
            }
            container.addView(tv)
        }

        val btnExit = findViewById<Button>(R.id.btnExitProgress)
        btnExit.setOnClickListener {
            finish()
        }
    }
}
