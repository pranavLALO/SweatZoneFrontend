package com.example.sweatzone.data.repository

import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.GenerateDietRequest

class DietRepository {

    suspend fun generateDietPlan(userId: Int, goal: String) =
        RetrofitClient.api.generateDietPlan(
            GenerateDietRequest(
                user_id = userId,
                goal = goal
            )
        )

    suspend fun getTodayDietPlan(userId: Int) =
        RetrofitClient.api.getTodayDietPlan(userId)
}
