package com.example.sweatzone.data.repository

import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.AuthResponse
import com.example.sweatzone.data.dto.LoginRequest
import com.example.sweatzone.data.dto.RegisterRequest

class AuthRepository {

    suspend fun login(email: String, password: String): Result<AuthResponse> {
        return try {
            val response = RetrofitClient.api.login(
                LoginRequest(email, password)
            )

            if (response.isSuccessful && response.body()?.status == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.body()?.message ?: "Login failed")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): Result<AuthResponse> {
        return try {
            val response = RetrofitClient.api.register(
                RegisterRequest(name, email, password)
            )

            if (response.isSuccessful && response.body()?.status == true) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception(response.body()?.message ?: "Registration failed")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
