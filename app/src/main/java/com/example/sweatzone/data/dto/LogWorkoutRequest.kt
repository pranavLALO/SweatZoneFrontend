package com.example.sweatzone.data.dto

data class LogWorkoutRequest(
    val user_id: Int,
    val muscle_group: String,
    val intensity: String,
    val duration: Int,
    val progress: Int
)
