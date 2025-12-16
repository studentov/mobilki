package com.example.spineguard.ui.exercises

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.spineguard.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ExercisesFragment : Fragment(R.layout.fragment_exercises) {

    private lateinit var adapter: ExerciseAdapter
    private lateinit var allExercises: List<Exercise>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvExercises = view.findViewById<RecyclerView>(R.id.rvExercises)
        val btnAll = view.findViewById<Button>(R.id.btnAll)
        val btnSitting = view.findViewById<Button>(R.id.btnSitting)
        val btnStanding = view.findViewById<Button>(R.id.btnStanding)

        allExercises = getAllExercises()

        // Передаем callback в адаптер
        adapter = ExerciseAdapter(allExercises) { exercise ->
            // Обработка клика по упражнению
            showExerciseDetails(exercise)
        }

        rvExercises.layoutManager = LinearLayoutManager(requireContext())
        rvExercises.adapter = adapter

        btnAll.setOnClickListener {
            adapter.updateList(allExercises)
        }

        btnSitting.setOnClickListener {
            adapter.updateList(allExercises.filter { it.type == ExerciseType.SITTING })
        }

        btnStanding.setOnClickListener {
            adapter.updateList(allExercises.filter { it.type == ExerciseType.STANDING })
        }
    }

    private fun showExerciseDetails(exercise: Exercise) {
        // Показываем диалог с деталями упражнения
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(exercise.title)
            .setMessage("${exercise.description}\n\nТип: ${getExerciseTypeText(exercise.type)}")
            .setPositiveButton("Закрыть") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun getExerciseTypeText(type: ExerciseType): String {
        return when (type) {
            ExerciseType.SITTING -> "Сидя"
            ExerciseType.STANDING -> "Стоя"
        }
    }

    private fun getAllExercises(): List<Exercise> {
        return listOf(
            Exercise(
                "Передний и задний наклоны таза",
                "Сидя на стуле: мягко округляйте/выпрямляйте поясницу, двигая таз вперёд-назад. 40–60 сек.",
                ExerciseType.SITTING
            ),
            Exercise(
                "Латеральные наклоны",
                "Сидя: наклон вправо, растягивая левый бок, затем в другую сторону. 40–60 сек.",
                ExerciseType.SITTING
            ),
            Exercise(
                "Вытяжение шеи",
                "Сидя: подбородок вперёд → назад (как двойной подбородок). По 2–3 сек в точке.",
                ExerciseType.SITTING
            ),
            Exercise(
                "Ротации сидя",
                "Сидя: руки в замок за головой, поворот грудной клетки влево/вправо. 40–60 сек.",
                ExerciseType.SITTING
            ),
            Exercise(
                "Круги плечами",
                "Стоя: поднимайте плечи вверх, сводите лопатки, затем опускайте вниз. 40–60 сек.",
                ExerciseType.STANDING
            ),
            Exercise(
                "Сгибания и разгибания грудного отдела",
                "Стоя: на вдохе руки в замок за спиной и потянуться, на выдохе округлить спину. 40–60 сек.",
                ExerciseType.STANDING
            ),
            Exercise(
                "Расслабление шеи",
                "Стоя: ладонь на плечо, наклон головы в сторону, мягкая растяжка. Затем поменять сторону.",
                ExerciseType.STANDING
            ),
            Exercise(
                "Заведение рук за спину",
                "Стоя: одну руку за затылок, другую за поясницу, попытаться соединить. Поменять стороны.",
                ExerciseType.STANDING
            )
        )
    }
}