package com.example.sweatzone.data.dto

data class MealDto(
    val id: Int,
    val meal_name: String,
    val meal_type: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fats: Int,
    val purpose: String,
    val is_vegetarian: Int
)
