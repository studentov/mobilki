package com.example.spineguard.ui.exercises

enum class ExerciseType {
    SITTING,
    STANDING
}

data class Exercise(
    val title: String,
    val description: String,
    val type: ExerciseType
)
