package com.example.sweatzone.data.dto

data class BodyProfileRequest(
    val user_id: Int,
    val height_cm: Int,
    val weight_kg: Double,
    val gender: String,
    val age: Int,
    val goal: String,
    val activity_level: String
)
