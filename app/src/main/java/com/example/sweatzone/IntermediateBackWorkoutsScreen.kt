package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.components.ExerciseItem
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun IntermediateBackWorkoutsScreen(navController: NavController, userViewModel: UserViewModel) {
    val pinkBg = Color(0xFFFFF0F5)
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }

    var exercises by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<List<com.example.sweatzone.data.dto.WorkoutExerciseDto>>(emptyList()) }
    var isLoading by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf(true) }
    var errorMessage by androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }

    androidx.compose.runtime.LaunchedEffect(Unit) {
        try {
            val response = com.example.sweatzone.data.api.RetrofitClient.api.getWorkoutExercises("back", "intermediate")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null && body.status) {
                    exercises = body.data ?: emptyList()
                } else {
                    errorMessage = body?.message ?: "Unknown API error"
                }
            } else {
                errorMessage = "HTTP Error: ${response.code()}"
            }
        } catch (e: Exception) {
            errorMessage = e.message ?: "Failed to connect to backend"
        } finally {
            isLoading = false
        }
    }


    val exerciseLogs = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateMapOf<Int, com.example.sweatzone.data.dto.ExerciseLog>() }

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                homeRoute = Screen.IntermediateHome.route,
                workoutsRoute = Screen.IntermediateWorkouts.route
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(pinkBg)
                .padding(innerPadding)
        ) {
            // Header
            item {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.backimg),
                        contentDescription = "Back Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Back",
                            color = Color.White,
                            fontSize = 36.sp,
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
            } else if (errorMessage != null) {
                item {
                    Text(
                        text = "Error: $errorMessage",
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            } else {
                items(exercises) { exercise ->
                    ExerciseItem(
                        trackingMode = "intermediate",
                        title = exercise.title,
                        videoUrl = "${com.example.sweatzone.data.api.RetrofitClient.BASE_URL}videos/${exercise.video_filename}",
                        instructions = exercise.instructions,
                        benefits = exercise.benefits,
                        plannedSets = exercise.sets ?: 3,
                        plannedReps = try { exercise.reps?.split("-")?.first()?.toInt() ?: 12 } catch(e: Exception) { 12 },
                        onDataChanged = { s, r ->
                            exerciseLogs[exercise.id] = com.example.sweatzone.data.dto.ExerciseLog(
                                exercise_title = exercise.title,
                                sets_completed = s,
                                reps_completed = r,
                                weight_kg = 0,
                                time_used_seconds = 0
                            )
                        }
                    )
                }
            }
            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        
                        val totalActualReps = exerciseLogs.values.sumOf { it.reps_completed }
                        val totalActualSets = exerciseLogs.values.sumOf { it.sets_completed }
                        
                        var totalPlannedReps = 0
                        exercises.forEach { ex ->
                            val s = ex.sets ?: 3
                            val r = try { ex.reps?.split("-")?.first()?.toInt() ?: 12 } catch(e: Exception) { 12 }
                            totalPlannedReps += (s * r)
                        }

                        userViewModel.setCurrentWorkoutResult(
                            WorkoutResult(
                                muscleGroup = "back",
                                intensity = "intermediate",
                                totalVolume = 0,
                                totalReps = totalActualReps,
                                totalSets = totalActualSets,
                                totalTimeSeconds = duration,
                                exerciseLogs = exerciseLogs.values.toList()
                            )
                        )

                        navController.navigate("workout_summary/back")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63))
                ) {
                    val logsCount = exerciseLogs.size
                    Text(
                        text = if (logsCount > 0) "Finish $logsCount Exercises" else "Finish Workout",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntermediateBackWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateBackWorkoutsScreen(
            navController = rememberNavController(),
            userViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
        )
    }
}
