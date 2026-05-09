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

    suspend fun getWater(userId: Int) =
        RetrofitClient.api.getWater(userId)

    suspend fun logWater(userId: Int, action: String) =
        RetrofitClient.api.logWater(
            com.example.sweatzone.data.dto.LogWaterRequest(
                user_id = userId,
                action = action
            )
        )
}
