package com.example.sweatzone.data.api

import com.example.sweatzone.data.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    // -------------------------
    // AUTH
    // -------------------------

    @POST("auth/register.php")
    @Headers("Content-Type: application/json")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("auth/login.php")
    @Headers("Content-Type: application/json")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    // -------------------------
    // BODY PROFILE (Height, Weight, Gender)
    // -------------------------

    @POST("profile/save_body_profile.php")
    @Headers("Content-Type: application/json")
    suspend fun saveBodyProfile(
        @Body request: BodyProfileRequest
    ): Response<BasicResponse>

    // -------------------------
    // MEALS (Browse)
    // -------------------------

    @GET("meals/get_meals.php")
    suspend fun getMeals(
        @Query("goal") goal: String
    ): Response<MealsResponse>

    // -------------------------
    // DIET PLAN
    // -------------------------

    @POST("diet/generate_diet_plan.php")
    @Headers("Content-Type: application/json")
    suspend fun generateDietPlan(
        @Body request: GenerateDietRequest
    ): Response<GenerateDietResponse>

    @GET("diet/get_today_plan.php")
    suspend fun getTodayDietPlan(
        @Query("user_id") userId: Int
    ): Response<TodayDietResponse>
}
