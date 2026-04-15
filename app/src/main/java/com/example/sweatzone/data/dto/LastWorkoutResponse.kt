package com.example.sweatzone.data.dto

data class LastWorkoutResponse(
    val status: Boolean,
    val message: String,
    val data: LastWorkoutData?
)

data class LastWorkoutData(
    val no: Int,
    val user_id: Int,
    val workout_date: String,
    val muscle_group: String,
    val intensity: String,
    val duration_seconds: Int,
    val progress_percentage: Int,
    val weight_kg: Int,
    val completed_sets: Int,
    val completed_reps: Int,
    val timer_seconds_used: Int,
    val exercise_logs: List<ExerciseLog>
)
