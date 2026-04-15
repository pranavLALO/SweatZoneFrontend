package com.example.sweatzone.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.sweatzone.WorkoutResult
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.Screen
import com.example.sweatzone.UserViewModel
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.ExerciseLog
import com.example.sweatzone.data.dto.WorkoutExerciseDto

@Composable
fun AdvancedWorkoutTemplate(
    navController: NavController,
    muscleGroup: String,
    headerResId: Int,
    title: String
) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val pinkBg = Color(0xFFFFF0F5)
    
    var exercises by remember { mutableStateOf<List<WorkoutExerciseDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    
    // Track logs per exercise ID
    val logsMap = remember { mutableStateMapOf<Int, ExerciseLog>() }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getWorkoutExercises(muscleGroup, "advanced")
            if (response.isSuccessful) {
                exercises = response.body()?.data ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

     Scaffolding_Internal(
        navController = navController,
        pinkBg = pinkBg,
        headerResId = headerResId,
        title = title,
        muscleGroup = muscleGroup,
        exercises = exercises,
        isLoading = isLoading,
        logsMap = logsMap,
        userViewModel = userViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun Scaffolding_Internal(
    navController: NavController,
    pinkBg: Color,
    headerResId: Int,
    title: String,
    muscleGroup: String,
    exercises: List<WorkoutExerciseDto>,
    isLoading: Boolean,
    logsMap: androidx.compose.runtime.snapshots.SnapshotStateMap<Int, ExerciseLog>,
    userViewModel: UserViewModel
) {
    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                homeRoute = Screen.AdvanceHome.route,
                workoutsRoute = Screen.AdvanceWorkouts.route
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(pinkBg)
                .padding(innerPadding)
        ) {
            // --- Header Image ---
            item {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = headerResId),
                        contentDescription = "$title Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "$title (Advanced)",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
            } else {
                items(exercises.size) { index ->
                    val exercise = exercises[index]
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        ExerciseItem(
                            title = exercise.title,
                            videoUrl = "${RetrofitClient.BASE_URL}videos/${exercise.video_filename}",
                            instructions = exercise.instructions,
                            benefits = exercise.benefits
                        )
                        
                        // Premium Tracker
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            PremiumAdvancedWorkoutTracker(
                                targetSets = exercise.sets ?: 3,
                                onExerciseSave = { s, r, w, t ->
                                    logsMap[exercise.id] = ExerciseLog(
                                        exercise_title = exercise.title,
                                        sets_completed = s,
                                        reps_completed = r,
                                        weight_kg = w,
                                        time_used_seconds = t
                                    )
                                }
                            )
                        }
                    }
                }
            }

            item {
                val isReady = logsMap.size >= exercises.size && exercises.isNotEmpty()
                
                Button(
                    onClick = {
                        val allLogs = logsMap.values.toList()
                        val totalVol = allLogs.sumOf { it.weight_kg * it.reps_completed * it.sets_completed }
                        val totalReps = allLogs.sumOf { it.reps_completed * it.sets_completed }
                        val totalSets = allLogs.sumOf { it.sets_completed }
                        val totalTime = allLogs.sumOf { it.time_used_seconds }
                        
                        userViewModel.setCurrentWorkoutResult(
                            WorkoutResult(
                                muscleGroup = muscleGroup,
                                intensity = "advanced",
                                totalVolume = totalVol,
                                totalReps = totalReps,
                                totalSets = totalSets,
                                totalTimeSeconds = totalTime,
                                exerciseLogs = allLogs
                            )
                        )
                        navController.navigate("workout_summary/$muscleGroup")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    enabled = isReady,
                    colors = ButtonDefaults.buttonColors(containerColor = if (isReady) Color.Black else Color.Gray),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = if (isReady) "SEE PERFORMANCE REPORT" else "COMPLETE ALL EXERCISES", color = Color.White)
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}
