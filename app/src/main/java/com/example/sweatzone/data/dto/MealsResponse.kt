package com.example.sweatzone.data.dto

data class MealsResponse(
    val status: Boolean,
    val message: String,
    val data: List<MealDto>
)
