package com.example.sweatzone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.GenerateDietRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * A ViewModel to hold and manage user-selected data across different screens.
 * This acts as a single source of truth for the user's goal, gender, etc.
 */
class UserViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UserSelectionState())
    val uiState: StateFlow<UserSelectionState> = _uiState.asStateFlow()

    fun setGoal(goal: String) {
        _uiState.update { currentState ->
            currentState.copy(goal = goal)
        }
    }
    fun updateGoal(goal: String) {
        _uiState.update { currentState ->
            currentState.copy(goal = goal)
        }
    }

    fun setGender(gender: String) {
        _uiState.update { currentState ->
            currentState.copy(gender = gender)
        }
    }
    fun updateGender(gender: String) {
        _uiState.update { currentState ->
            currentState.copy(gender = gender)
        }
    }

    fun completeOnboarding() {
        _uiState.update { currentState ->
            currentState.copy(onboardingComplete = true)
        }
    }

    var userId: Int = 1 // temporary for testing

    fun generateDietPlan(
        goal: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.generateDietPlan(
                    GenerateDietRequest(
                        user_id = userId,
                        goal = goal
                    )
                )

                if (response.isSuccessful && response.body()?.status == true) {
                    onSuccess()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

/**
 * Represents the user's choices made during the onboarding process.
 */
data class UserSelectionState(
    val goal: String? = null,
    val gender: String? = null,
    val onboardingComplete: Boolean = false // Flag to check if initial setup is done
)
