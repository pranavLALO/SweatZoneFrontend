package com.example.sweatzone.viewmodel

import com.example.sweatzone.data.dto.TodayDietResponse

sealed class DietState {
    object Idle : DietState()
    object Loading : DietState()
    object Success : DietState()
    data class TodayPlan(val data: TodayDietResponse) : DietState()
    data class Error(val message: String) : DietState()
}
