package com.example.sweatzone.data.dto

data class StatsResponse(
    val status: Boolean,
    val message: String,
    val data: List<WorkoutStat>,
    val total_completed: Int = 0
)

data class WorkoutStat(
    val date: String? = null,
    val count: Int,
    val day: String,
    val progress: Int = 0
)
