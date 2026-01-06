package com.example.sweatzone.data.dto

data class AuthResponse(
    val status: Boolean,
    val message: String,
    val user: User? = null
)

data class User(
    val id: Int,
    val name: String,
    val email: String
)
