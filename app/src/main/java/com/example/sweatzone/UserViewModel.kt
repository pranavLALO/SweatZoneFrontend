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
import com.example.sweatzone.data.dto.ExerciseLog
import com.example.sweatzone.data.dto.WorkoutExercisesResponse

import com.example.sweatzone.data.dto.WorkoutHistoryItem
import com.example.sweatzone.data.dto.WorkoutHistoryResponse

/**
 * A ViewModel to hold and manage user-selected data across different screens.
 * This acts as a single source of truth for the user's goal, gender, etc.
 */
class UserViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UserSelectionState())
    val uiState: StateFlow<UserSelectionState> = _uiState.asStateFlow()

    // --- WORKOUT HISTORY & PROGRESS ---
    private val _workoutHistory = MutableStateFlow<List<WorkoutHistoryItem>?>(null)
    val workoutHistory: StateFlow<List<WorkoutHistoryItem>?> = _workoutHistory.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchWorkoutHistory() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getWorkoutHistory()
                if (response.isSuccessful && response.body()?.status == true) {
                    _workoutHistory.value = response.body()?.data
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- ADVANCED WORKOUT SESSION STATE ---
    private val _currentWorkoutResult = MutableStateFlow<WorkoutResult?>(null)
    val currentWorkoutResult: StateFlow<WorkoutResult?> = _currentWorkoutResult.asStateFlow()

    fun setCurrentWorkoutResult(result: WorkoutResult) {
        _currentWorkoutResult.value = result
    }

    fun clearCurrentWorkoutResult() {
        _currentWorkoutResult.value = null
    }

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

    fun logWorkout(
        muscleGroup: String,
        intensity: String,
        durationSeconds: Int = 0,
        weightKg: Int = 0,
        completedSets: Int = 0,
        targetSets: Int = 3,
        completedReps: Int = 0,
        targetReps: Int = 10,
        timerUsed: Int = 0,
        exerciseLogs: List<ExerciseLog>? = null,
        onError: (String) -> Unit = {},
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Calculate progress based on logic:
                val progress = if (intensity.lowercase() == "high" || intensity.lowercase() == "advanced") {
                    // --- ADVANCED INTELLIGENT FORMULA (0-100) ---
                    // 1. Volume Factor (40%): How much of the planned work was done?
                    val safeTargetSets = if (targetSets > 0) targetSets else 3
                    val safeTargetReps = if (targetReps > 0) targetReps else 10
                    val volumeFactor = (completedSets.toFloat() / safeTargetSets) * (completedReps.toFloat() / safeTargetReps)
                    
                    // 2. Intensity Factor (30%): Based on weight used (max 50kg)
                    val intensityFactor = (weightKg.toFloat() / 50f).coerceIn(0f, 1f)
                    
                    // 3. Consistency Factor (30%): Based on 180s timer utilization
                    val consistencyFactor = (timerUsed.toFloat() / 180f).coerceIn(0f, 1f)
                    
                    // Final weighted score
                    ((volumeFactor * 40) + (intensityFactor * 30) + (consistencyFactor * 30)).toInt().coerceIn(0, 100)
                } else if (intensity.lowercase() == "medium" || intensity.lowercase() == "intermediate") {
                    // --- INTERMEDIATE REPS-BASED FORMULA (0-100) ---
                    val totalPlanned = (targetSets * targetReps).toFloat()
                    if (totalPlanned > 0) {
                        ((completedReps.toFloat() / totalPlanned) * 100).toInt().coerceIn(0, 100)
                    } else {
                        70 // Default
                    }
                } else {
                    // Standard logic for Beginner
                    when {
                        durationSeconds < 300 -> 30
                        durationSeconds < 600 -> 60
                        else -> 100
                    }
                }

                val response = RetrofitClient.api.logWorkout(
                    LogWorkoutRequest(
                        user_id = userId,
                        muscle_group = muscleGroup,
                        intensity = intensity,
                        duration = durationSeconds,
                        progress = progress,
                        weight_kg = if (weightKg > 0) weightKg else null,
                        completed_sets = if (completedSets > 0) completedSets else null,
                        completed_reps = if (completedReps > 0) completedReps else null,
                        timer_seconds_used = if (timerUsed > 0) timerUsed else null,
                        exercise_logs = exerciseLogs
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

    private val _lastWorkout = MutableStateFlow<com.example.sweatzone.data.dto.LastWorkoutResponse?>(null)
    val lastWorkout: StateFlow<com.example.sweatzone.data.dto.LastWorkoutResponse?> = _lastWorkout.asStateFlow()

    fun fetchLastWorkout(muscleGroup: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getLastWorkout(muscleGroup)
                if (response.isSuccessful) {
                    _lastWorkout.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    // --- CUSTOM ROUTINES & MEDALS ---
    private val _customRoutines = MutableStateFlow<List<com.example.sweatzone.data.dto.CustomRoutine>>(emptyList())
    val customRoutines: StateFlow<List<com.example.sweatzone.data.dto.CustomRoutine>> = _customRoutines.asStateFlow()

    private val _userBadges = MutableStateFlow<List<com.example.sweatzone.data.dto.BadgeDto>>(emptyList())
    val userBadges: StateFlow<List<com.example.sweatzone.data.dto.BadgeDto>> = _userBadges.asStateFlow()

    fun fetchCustomRoutines() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = RetrofitClient.api.getCustomRoutines()
                if (response.isSuccessful && response.body()?.status == true) {
                    _customRoutines.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun fetchUserBadges() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getUserBadges()
                if (response.isSuccessful && response.body()?.status == true) {
                    _userBadges.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class UserSelectionState(
    val goal: String? = null,
    val gender: String? = null,
    val height: Int? = null,
    val weight: Double? = null,
    val onboardingComplete: Boolean = false
)

data class WorkoutResult(
    val muscleGroup: String,
    val intensity: String,
    val totalVolume: Int,
    val totalReps: Int,
    val totalSets: Int,
    val totalTimeSeconds: Int,
    val exerciseLogs: List<ExerciseLog>
)
