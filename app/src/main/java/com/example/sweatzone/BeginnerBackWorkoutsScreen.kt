package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.WorkoutExerciseDto
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.components.ExerciseItem
import com.example.sweatzone.ui.theme.SweatzoneTheme
import kotlinx.coroutines.launch

@Composable
fun BeginnerBackWorkoutsScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val context = LocalContext.current
    val startTime = remember { System.currentTimeMillis() }
    val pinkBg = Color(0xFFFFF0F5)
    val coroutineScope = rememberCoroutineScope()
    
    // State to hold fetched workouts
    var exercises by remember { mutableStateOf<List<WorkoutExerciseDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Fetch dynamic content on load
    LaunchedEffect(Unit) {
        try {
            val response = RetrofitClient.api.getWorkoutExercises("back", "beginner")
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

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController, homeRoute = Screen.BeginnerHome.route)
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
                        title = exercise.title,
                        videoUrl = "${RetrofitClient.BASE_URL}videos/${exercise.video_filename}",
                        instructions = exercise.instructions,
                        benefits = exercise.benefits
                    )
                }
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout(
                            muscleGroup = "back", 
                            intensity = "medium",
                            durationSeconds = duration,
                            onSuccess = { 
                                navController.popBackStack() 
                            },
                            onError = { errorMsg ->
                                android.widget.Toast.makeText(context, "Error: $errorMsg", android.widget.Toast.LENGTH_LONG).show()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63))
                ) {
                    Text(text = "Finish Workout", color = Color.Black)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeginnerBackWorkoutsScreenPreview() {
    SweatzoneTheme {
        BeginnerBackWorkoutsScreen(navController = rememberNavController())
    }
}
