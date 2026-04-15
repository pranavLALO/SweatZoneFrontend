package com.example.sweatzone.data.dto

data class WorkoutHistoryResponse(
    val status: Boolean,
    val data: List<WorkoutHistoryItem>?,
    val message: String?
)

data class WorkoutHistoryItem(
    val id: Int,
    val muscle_group: String,
    val intensity: String,
    val duration_seconds: Int,
    val weight_kg: Int,
    val completed_sets: Int,
    val completed_reps: Int,
    val completed_at: String
)
