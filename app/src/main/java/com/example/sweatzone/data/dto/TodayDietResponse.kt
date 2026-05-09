package com.example.sweatzone.data.dto

data class TodayDietResponse(
    val status: Boolean,
    val date: String,
    val total_calories: Int = 0,
    val meals: Map<String, List<DietMealDto>>
)
