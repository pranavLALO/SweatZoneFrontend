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

    @POST("auth/forgot_password.php")
    @Headers("Content-Type: application/json")
    suspend fun forgotPassword(
        @Body request: ForgotPasswordRequest
    ): Response<BasicResponse>

    @POST("auth/verify_otp.php")
    @Headers("Content-Type: application/json")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<BasicResponse>

    @POST("auth/reset_password.php")
    @Headers("Content-Type: application/json")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<BasicResponse>

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
    @GET("profile/get_body_profile.php")
    suspend fun getBodyProfile(
        @Query("user_id") userId: Int
    ): Response<BodyProfileResponse>

    @GET("meals/get_meals.php")
    suspend fun getMeals(
        @Query("goal") goal: String,
        @Query("is_vegetarian") isVegetarian: Int
    ): Response<MealsResponse>

    // -------------------------
    // DIET PLAN
    // -------------------------

    @POST("diet/generate_diet_plan.php")
    @Headers("Content-Type: application/json")
    suspend fun generateDietPlan(
        @Body request: GenerateDietRequest
    ): Response<GenerateDietResponse>

    @POST("workouts/log.php")
    @Headers("Content-Type: application/json")
    suspend fun logWorkout(
        @Body request: LogWorkoutRequest
    ): Response<BasicResponse>

    @GET("diet/get_today_plan.php")
    suspend fun getTodayDietPlan(
        @Query("user_id") userId: Int
    ): Response<TodayDietResponse>

    @GET("workouts/stats.php")
    suspend fun getWorkoutStats(
        @Query("period") period: String = "week"
    ): Response<StatsResponse>

    // -------------------------
    // FORM SCORES
    // -------------------------

    @POST("workouts/save_form_score.php")
    @Headers("Content-Type: application/json")
    suspend fun saveFormScore(
        @Body request: SaveScoreRequest
    ): Response<BasicResponse>

    @GET("workouts/get_top_scores.php")
    suspend fun getTopScores(
        @Query("user_id") userId: Int,
        @Query("exercise_name") exerciseName: String? = null
    ): Response<TopScoresResponse>

    // -------------------------
    // WORKOUT DATA
    // -------------------------
    @GET("workouts/get_last_workout.php")
    suspend fun getLastWorkout(
        @Query("muscle_group") muscleGroup: String
    ): Response<LastWorkoutResponse>

    @GET("workouts/get_workout_history.php")
    suspend fun getWorkoutHistory(): Response<WorkoutHistoryResponse>

    @GET("get_workout_exercises.php")
    suspend fun getWorkoutExercises(
        @Query("target_muscle") targetMuscle: String,
        @Query("difficulty") difficulty: String
    ): Response<WorkoutExercisesResponse>

    @GET("workouts/get_library.php")
    suspend fun getLibrary(): Response<LibraryResponse>

    @POST("workouts/save_custom_routine.php")
    @Headers("Content-Type: application/json")
    suspend fun saveCustomRoutine(
        @Body request: SaveCustomRoutineRequest
    ): Response<BasicResponse>

    @GET("workouts/get_custom_routines.php")
    suspend fun getCustomRoutines(): Response<CustomRoutinesResponse>

    @GET("workouts/get_custom_routine_by_id.php")
    suspend fun getCustomRoutineById(
        @Query("routine_id") routineId: Int
    ): Response<SingleCustomRoutineResponse>

    @POST("workouts/update_custom_routine.php")
    @Headers("Content-Type: application/json")
    suspend fun updateCustomRoutine(
        @Body request: UpdateCustomRoutineRequest
    ): Response<BasicResponse>

    @GET("workouts/get_user_badges.php")
    suspend fun getUserBadges(): Response<BadgeResponse>
}
