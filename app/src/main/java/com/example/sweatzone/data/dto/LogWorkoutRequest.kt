package com.example.sweatzone.data.dto

data class LogWorkoutRequest(
    val user_id: Int,
    val muscle_group: String,
    val intensity: String,
    val duration: Int,
    val progress: Int,
    val weight_kg: Int? = null,
    val completed_sets: Int? = null,
    val completed_reps: Int? = null,
    val timer_seconds_used: Int? = null,
    val exercise_logs: List<ExerciseLog>? = null
)

data class ExerciseLog(
    val exercise_title: String,
    val sets_completed: Int,
    val reps_completed: Int,
    val weight_kg: Int,
    val time_used_seconds: Int
)
