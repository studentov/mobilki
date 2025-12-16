package com.example.spineguard.ui.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spineguard.R

class ExerciseAdapter(
    private var exercises: List<Exercise>,
    private val onExerciseClick: (Exercise) -> Unit // Добавляем callback
) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    class ExerciseViewHolder(
        itemView: View,
        private val onExerciseClick: (Exercise) -> Unit // Передаем в ViewHolder
    ) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvExerciseTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvExerciseDescription)
        val tvType: TextView = itemView.findViewById(R.id.tvExerciseType)

        fun bind(exercise: Exercise) {
            tvTitle.text = exercise.title
            tvDescription.text = exercise.description
            tvType.text = when (exercise.type) {
                ExerciseType.SITTING -> "Сидя"
                ExerciseType.STANDING -> "Стоя"
            }

            // Устанавливаем обработчик клика на весь элемент
            itemView.setOnClickListener {
                onExerciseClick(exercise)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise, parent, false)
        return ExerciseViewHolder(view, onExerciseClick)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val item = exercises[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = exercises.size

    fun updateList(newList: List<Exercise>) {
        exercises = newList
        notifyDataSetChanged()
    }
}