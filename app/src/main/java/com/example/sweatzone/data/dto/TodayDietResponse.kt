package com.example.sweatzone.data.dto

data class TodayDietResponse(
    val status: Boolean,
    val date: String,
    val meals: Map<String, List<DietMealDto>>
)
