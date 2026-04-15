package com.example.sweatzone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.WorkoutStat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _state = MutableStateFlow<ProfileState>(ProfileState.Idle)
    val state: StateFlow<ProfileState> = _state

    private val _workoutHistory = MutableStateFlow<List<com.example.sweatzone.data.dto.WorkoutHistoryItem>>(emptyList())
    val workoutHistory: StateFlow<List<com.example.sweatzone.data.dto.WorkoutHistoryItem>> = _workoutHistory

    private val _customRoutines = MutableStateFlow<List<com.example.sweatzone.data.dto.CustomRoutine>>(emptyList())
    val customRoutines: StateFlow<List<com.example.sweatzone.data.dto.CustomRoutine>> = _customRoutines

    fun loadProfile(userId: Int) {
        viewModelScope.launch {
            _state.value = ProfileState.Loading
            try {
                val profileResponse = RetrofitClient.api.getBodyProfile(userId)
                
                // Default stats to week on first load
                val statsResponse = RetrofitClient.api.getWorkoutStats("week")

                if (profileResponse.isSuccessful && profileResponse.body()?.status == true) {
                    val profile = profileResponse.body()!!.body_profile
//                    val stats = if (statsResponse.isSuccessful && statsResponse.body()?.status == true) {
//                        statsResponse.body()!!.data
//                    } else {
//                        emptyList()
//                    }
                    var stats = emptyList<WorkoutStat>()
                    var total = 0
                    if (statsResponse.isSuccessful && statsResponse.body()?.status == true) {
                        stats = statsResponse.body()!!.data
                        total = statsResponse.body()!!.total_completed
                    }

                    if (profile != null) {
                        // Persist activity level, gender and goal globally to fix navigation resets and enable gender-aware goal routing
                        com.example.sweatzone.data.local.TokenManager.saveUserLevel(profile.activity_level)
                        com.example.sweatzone.data.local.TokenManager.saveUserGender(profile.gender)
                        com.example.sweatzone.data.local.TokenManager.saveUserGoal(profile.goal)
                        
                        // Fetch History concurrently for the UI
                        try {
                            val historyResponse = RetrofitClient.api.getWorkoutHistory()
                            if (historyResponse.isSuccessful && historyResponse.body()?.status == true) {
                                _workoutHistory.value = historyResponse.body()?.data ?: emptyList()
                            }
                        } catch (e: Exception) {
                            // Silently fail history if offline, don't break profile
                        }

                        // Fetch Custom Routines concurrently
                        try {
                            val routineResponse = RetrofitClient.api.getCustomRoutines()
                            if (routineResponse.isSuccessful && routineResponse.body()?.status == true) {
                                _customRoutines.value = routineResponse.body()?.data ?: emptyList()
                            }
                        } catch (e: Exception) {
                        }

                        _state.value = ProfileState.Success(
                            profile = profile,
                            weeklyStats = stats,
                            graphStats = stats,
                            totalWorkouts = total
                        )
                    } else {
                        _state.value = ProfileState.Error("Profile empty")
                    }
                } else {
                    val errorBody = profileResponse.errorBody()?.string()
                    val message = if (!errorBody.isNullOrEmpty()) {
                        try {
                            com.google.gson.Gson().fromJson(errorBody, com.example.sweatzone.data.dto.BodyProfileResponse::class.java).message ?: "Failed to load profile"
                        } catch (e: Exception) {
                            "Failed to load profile"
                        }
                    } else {
                        profileResponse.body()?.message ?: "Failed to load profile"
                    }
                    _state.value = ProfileState.Error(message)
                }
            } catch (e: Exception) {
                val errorMsg = if (e is IllegalStateException && e.message?.contains("Expected BEGIN_OBJECT") == true) {
                    "Invalid Server Response. If using DevTunnel, please open the URL in a browser to accept the warning, or check your API URL."
                } else {
                    e.message ?: "Network error"
                }
                _state.value = ProfileState.Error(errorMsg)
            }
        }
    }

    fun updateStatsPeriod(period: String) {
        viewModelScope.launch {
            val currentState = _state.value
            if (currentState is ProfileState.Success) {
                try {
                    val newStats = when (period) {
                        "year" -> {
                             // Mock Year data for 2026
                             listOf(
                                 WorkoutStat(day="Jan", count=12),
                                 WorkoutStat(day="Feb", count=15),
                                 WorkoutStat(day="Mar", count=20),
                                 WorkoutStat(day="Apr", count=18),
                                 WorkoutStat(day="May", count=22),
                                 WorkoutStat(day="Jun", count=25),
                                 WorkoutStat(day="Jul", count=30),
                                 WorkoutStat(day="Aug", count=28),
                                 WorkoutStat(day="Sep", count=26),
                                 WorkoutStat(day="Oct", count=24),
                                 WorkoutStat(day="Nov", count=20),
                                 WorkoutStat(day="Dec", count=15)
                             )
                        }
                        "month" -> {
                            // Mock Month data
                             val list = mutableListOf<WorkoutStat>()
                             for (i in 1..30) {
                                 val count = if (i % 3 == 0) (5..40).random() else 0
                                 list.add(WorkoutStat(day=i.toString(), count=count))
                             }
                             list
                        }
                        else -> {
                             // "week" - fetch from API
                             val statsResponse = RetrofitClient.api.getWorkoutStats(period)
                             if (statsResponse.isSuccessful && statsResponse.body()?.status == true) {
                                 statsResponse.body()!!.data
                             } else {
                                 emptyList() // Clear stats on failure to avoid showing stale data from other periods
                             }
                        }
                    }
                    
                    // Update state
                    _state.value = currentState.copy(graphStats = newStats)

                } catch (e: Exception) {
                    // Keep old data on error
                }
            }
        }
    }
}
