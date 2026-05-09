package com.example.sweatzone.data.dto

data class WeeklySummaryResponse(
    val status: Boolean,
    val total_workouts: Int,
    val total_volume: Int,
    val total_time: Int,
    val avg_calories: Int,
    val total_water: Int
)
