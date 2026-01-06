package com.example.sweatzone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweatzone.data.repository.DietRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DietViewModel : ViewModel() {

    private val repository = DietRepository()

    private val _dietState = MutableStateFlow<DietState>(DietState.Idle)
    val dietState: StateFlow<DietState> = _dietState

    fun generateDietPlan(userId: Int, goal: String) {
        viewModelScope.launch {
            _dietState.value = DietState.Loading
            try {
                val response = repository.generateDietPlan(userId, goal)
                if (response.isSuccessful && response.body()?.status == true) {
                    _dietState.value = DietState.Success
                } else {
                    _dietState.value = DietState.Error("Failed to generate plan")
                }
            } catch (e: Exception) {
                _dietState.value = DietState.Error(e.message ?: "Network error")
            }
        }
    }

    fun getTodayDietPlan(userId: Int) {
        viewModelScope.launch {
            _dietState.value = DietState.Loading
            try {
                val response = repository.getTodayDietPlan(userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _dietState.value = DietState.TodayPlan(response.body()!!)
                } else {
                    _dietState.value = DietState.Error("No diet found")
                }
            } catch (e: Exception) {
                _dietState.value = DietState.Error(e.message ?: "Network error")
            }
        }
    }

    fun resetState() {
        _dietState.value = DietState.Idle
    }
}
