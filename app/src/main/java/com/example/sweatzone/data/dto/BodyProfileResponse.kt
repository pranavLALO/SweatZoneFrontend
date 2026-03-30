package com.example.sweatzone.data.dto

data class BodyProfileResponse(
    val status: Boolean,
    val message: String? = null,
    val body_profile: BodyProfileDto? = null
)

data class BodyProfileDto(
    val height_cm: Int,
    val weight_kg: Int,
    val gender: String,
    val goal: String,
    val activity_level: String,
    val name: String?
)
