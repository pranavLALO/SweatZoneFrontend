package com.example.sweatzone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.GenerateDietRequest
import com.example.sweatzone.data.dto.LogWorkoutRequest
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

    fun setHeight(height: Int) {
        _uiState.update { it.copy(height = height) }
    }
    fun setWeight(weight: Double) {
        _uiState.update { it.copy(weight = weight) }
    }

    fun saveBodyProfile(
        activityLevel: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            val state = uiState.value
            try {
                // If fields are missing, maybe use defaults or return
                val response = RetrofitClient.api.saveBodyProfile(
                    com.example.sweatzone.data.dto.BodyProfileRequest(
                        user_id = userId,
                        height_cm = state.height ?: 170,
                        weight_kg = state.weight ?: 70.0,
                        gender = state.gender ?: "Male",
                        age = 25, 
                        goal = state.goal ?: "maintain",
                        activity_level = activityLevel
                    )
                )
                if (response.isSuccessful && response.body()?.status == true) {
                    // Critical: Persist level, gender and goal locally
                    com.example.sweatzone.data.local.TokenManager.saveUserLevel(activityLevel)
                    state.gender?.let { com.example.sweatzone.data.local.TokenManager.saveUserGender(it) }
                    state.goal?.let { com.example.sweatzone.data.local.TokenManager.saveUserGoal(it) }
                    onSuccess()
                } else {
                    onError(response.body()?.message ?: "Save Failed")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e.message ?: "Unknown Error")
            }
        }
    }

    fun completeOnboarding() {
        _uiState.update { currentState ->
            currentState.copy(onboardingComplete = true)
        }
    }

    // userId is retrieved from TokenManager
    val userId: Int
        get() = com.example.sweatzone.data.local.TokenManager.getUserId()

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

    fun logWorkout(muscleGroup: String, intensity: String, durationSeconds: Int, onError: (String) -> Unit = {}, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                // Calculate progress based on logic:
                // < 1 min (60s) -> 30%
                // 4 min (240s) -> 60%
                // 8 min (480s) -> 80%
                // 13+ min (780s) -> 90%
                val progress = when {
                    durationSeconds < 60 -> 30
                    durationSeconds < 240 -> 30 // Wait, strict interpretation: <1m=30, 4m means 60. So 1-4m? Assuming 30 until 4m.
                    durationSeconds < 480 -> 60
                    durationSeconds < 780 -> 80
                    else -> 90
                }

                val response = RetrofitClient.api.logWorkout(
                    LogWorkoutRequest(
                        user_id = userId,
                        muscle_group = muscleGroup,
                        intensity = intensity,
                        duration = durationSeconds,
                        progress = progress
                    )
                )
                if (response.isSuccessful && response.body()?.status == true) {
                    onSuccess()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val message = if (!errorBody.isNullOrEmpty()) {
                        try {
                            com.google.gson.Gson().fromJson(errorBody, com.example.sweatzone.data.dto.BasicResponse::class.java).message ?: "Failed to log workout"
                        } catch (e: Exception) {
                            "Failed to log workout"
                        }
                    } else {
                        response.body()?.message ?: "Failed to log workout"
                    }
                    onError(message)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                onError(e.message ?: "Network error")
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
    val height: Int? = null,
    val weight: Double? = null,
    val onboardingComplete: Boolean = false // Flag to check if initial setup is done
)
