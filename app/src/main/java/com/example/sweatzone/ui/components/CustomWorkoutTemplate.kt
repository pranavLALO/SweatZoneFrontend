package com.example.sweatzone.ui.components

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.R
import com.example.sweatzone.Screen
import com.example.sweatzone.UserViewModel
import com.example.sweatzone.WorkoutResult
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.ExerciseLog
import com.example.sweatzone.data.dto.WorkoutExerciseDto
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.Icons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomWorkoutTemplate(
    navController: NavController,
    routineId: Int,
    userViewModel: UserViewModel
) {
    val pinkBg = Color(0xFF121212)
    var routineName by remember { mutableStateOf("Custom Routine") }
    var exercises by remember { mutableStateOf<List<WorkoutExerciseDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    // Track logs per exercise ID
    val logsMap = remember { mutableStateMapOf<Int, ExerciseLog>() }

    // Progression Stats (PB)
    val userBadges by userViewModel.userBadges.collectAsState()
    val personalBests = remember { mutableStateMapOf<Int, Int>() } // Exercise ID -> Max Reps

    LaunchedEffect(routineId) {
        userViewModel.fetchUserBadges()
        try {
            val response = RetrofitClient.api.getCustomRoutineById(routineId)
            if (response.isSuccessful && response.body()?.status == true) {
                val data = response.body()?.data
                if (data != null) {
                    routineName = data.routine_name
                    exercises = data.exercises
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(routineName, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("MASTER SESSION", color = Color(0xFFE0FF63), fontSize = 10.sp, letterSpacing = 2.sp)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        // Navigate back to library in 'Edit' mode for this routine
                        navController.navigate("workout_library") 
                    }) {
                        Text("ADD WORKOUTS", color = Color(0xFFE0FF63), fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF1E1E1E))
            )
        },
        containerColor = pinkBg
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = routineName.uppercase(),
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 36.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Focus: ${exercises.joinToString(", ") { it.target_muscle }.split(", ").distinct().joinToString(", ")}",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }

            if (isLoading) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color(0xFFE0FF63))
                    }
                }
            } else if (exercises.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No exercises found", color = Color.Gray)
                    }
                }
            } else {
                items(exercises.size) { index ->
                    val exercise = exercises[index]
                    Column(modifier = Modifier.padding(bottom = 24.dp)) {
                        ExerciseItem(
                            title = exercise.title,
                            videoUrl = "${RetrofitClient.BASE_URL}videos/${exercise.video_filename}",
                            instructions = exercise.instructions,
                            benefits = exercise.benefits,
                            trackingMode = "none"
                        )

                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            // Professional Tracker with PB integration
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
                                muscleGroup = routineName,
                                intensity = "advanced",
                                totalVolume = totalVol,
                                totalReps = totalReps,
                                totalSets = totalSets,
                                totalTimeSeconds = totalTime,
                                exerciseLogs = allLogs
                            )
                        )
                        
                        // Check for New PB or Milestone Achievement
                        // For now, we'll navigate to summary which can also show the medal animation
                        navController.navigate("workout_summary/${routineName.replace("/", "_")}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(64.dp),
                    enabled = isReady,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isReady) Color(0xFFE0FF63) else Color.DarkGray,
                        disabledContainerColor = Color.DarkGray
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = if (isReady) "CLAIM YOUR REWARDS" else "TRACK ALL EXERCISES TO FINISH",
                        color = if (isReady) Color.Black else Color.Gray,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}
