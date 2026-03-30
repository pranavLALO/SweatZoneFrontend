package com.example.sweatzone.viewmodel

import com.example.sweatzone.data.dto.BodyProfileDto
import com.example.sweatzone.data.dto.WorkoutStat

sealed class ProfileState {
    object Idle : ProfileState()
    object Loading : ProfileState()
    data class Success(
        val profile: BodyProfileDto,
        val weeklyStats: List<WorkoutStat> = emptyList(),
        val graphStats: List<WorkoutStat> = emptyList(),
        val totalWorkouts: Int = 0
    ) : ProfileState()
    data class Error(val message: String) : ProfileState()
}
