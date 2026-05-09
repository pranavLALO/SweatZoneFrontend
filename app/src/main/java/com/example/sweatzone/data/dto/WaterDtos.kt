package com.example.sweatzone.data.dto

data class WaterResponse(
    val status: Boolean,
    val date: String?,
    val glasses: Int,
    val message: String?
)

data class LogWaterRequest(
    val user_id: Int,
    val action: String // "add" or "remove"
)
