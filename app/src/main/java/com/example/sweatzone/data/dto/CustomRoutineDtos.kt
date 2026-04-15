package com.example.sweatzone.data.dto

data class LibraryResponse(
    val status: Boolean,
    val message: String?,
    val data: List<WorkoutExerciseDto>?
)

data class CustomRoutinesResponse(
    val status: Boolean,
    val message: String?,
    val data: List<CustomRoutine>?
)

data class SingleCustomRoutineResponse(
    val status: Boolean,
    val message: String?,
    val data: CustomRoutine?
)

data class CustomRoutine(
    val id: Int,
    val routine_name: String,
    val created_at: String,
    val exercises: List<WorkoutExerciseDto>
)

data class SaveCustomRoutineRequest(
    val routine_name: String,
    val exercise_ids: List<Int>
)

data class UpdateCustomRoutineRequest(
    val routine_id: Int,
    val routine_name: String?,
    val exercise_ids: List<Int>?
)

data class BadgeResponse(
    val status: Boolean,
    val message: String?,
    val data: List<BadgeDto>?
)

data class BadgeDto(
    val routine_name: String,
    val type: String,
    val tier: String,
    val message: String
)
