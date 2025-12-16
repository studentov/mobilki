package com.example.spineguard.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.spineguard.R
import com.google.android.material.card.MaterialCardView

class DashboardFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cardPosture: MaterialCardView = view.findViewById(R.id.cardPosture)
        val cardExercises: MaterialCardView = view.findViewById(R.id.cardExercises)
        val cardProgress: MaterialCardView = view.findViewById(R.id.cardProgress)

        cardPosture.setOnClickListener {
            Toast.makeText(requireContext(), "Проверка осанки (пока заглушка)", Toast.LENGTH_SHORT).show()
        }

        cardExercises.setOnClickListener {
            Toast.makeText(requireContext(), "Список упражнений (пока заглушка)", Toast.LENGTH_SHORT).show()
        }

        cardProgress.setOnClickListener {
            Toast.makeText(requireContext(), "Мой прогресс (пока заглушка)", Toast.LENGTH_SHORT).show()
        }
    }
}
