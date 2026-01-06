package com.example.sweatzone.data.dto

data class DietMealDto(
    val meal_name: String,
    val meal_type: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val meal_time: String
)
