package com.example.sweatzone.data.dto

data class WorkoutExercisesResponse(
    val status: Boolean,
    val data: List<WorkoutExerciseDto>? = null,
    val message: String? = null
)

data class WorkoutExerciseDto(
    val id: Int,
    val target_muscle: String,
    val difficulty: String,
    val title: String,
    val video_filename: String,
    val instructions: List<String>,
    val benefits: List<String>,
    val sets: Int? = null,
    val reps: String? = null
)
