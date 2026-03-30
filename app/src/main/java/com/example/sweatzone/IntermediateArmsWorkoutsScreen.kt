package com.example.sweatzone

import androidx.compose.foundation.Image
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
fun IntermediateArmsWorkoutsScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val pinkBg = Color(0xFFFFF0F5)
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }

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
                        painter = painterResource(id = R.drawable.armsimg), // Make sure you have armsimg.png/jpg
                        contentDescription = "Arms Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Arms",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            // Bicep Exercises
            item {
                ExerciseItem(
                    title = "Barbell Curl",
                    videoResId = R.raw.barbell_rows_video, // Replace with barbell_curl_video
                    instructions = listOf(
                        "Stand up straight with your torso upright, holding a barbell at a shoulder-width grip.",
                        "While holding the upper arms stationary, curl the weights forward while contracting the biceps.",
                        "Continue the movement until your biceps are fully contracted and the bar is at shoulder level.",
                        "Hold the contracted position for a second and squeeze the biceps hard.",
                        "Slowly begin to bring the bar back to the starting position."
                    ),
                    benefits = listOf(
                        "Builds bigger and stronger biceps through heavy loading.",
                        "Engages the forearms and improves grip strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Incline Dumbbell Curl",
                    videoResId = R.raw.incline_curl_video, // Replace with incline_curl_video
                    instructions = listOf(
                        "Sit on an incline bench at a 45-60 degree angle.",
                        "Let your arms hang straight down, holding a dumbbell in each hand.",
                        "Keep your elbows stationary and behind your torso throughout the movement.",
                        "Curl the weights up towards your shoulders, squeezing the biceps at the top.",
                        "Slowly lower the dumbbells back to the starting position."
                    ),
                    benefits = listOf(
                        "Targets the long head of the bicep for better peak development.",
                        "Provides maximum isolation due to the seated, inclined position."
                    )
                )
            }

            // Tricep Exercises
            item {
                ExerciseItem(
                    title = "Dips",
                    videoResId = R.raw.dips_video, // Replace with dips_video
                    instructions = listOf(
                        "Position your hands on parallel bars with your arms fully extended.",
                        "Lower your body by bending your elbows until they are at a 90-degree angle.",
                        "Continue lowering until your shoulders are just below your elbows to feel a stretch.",
                        "Push your body back up to the starting position by straightening your arms.",
                        "Keep your torso upright to emphasize the triceps."
                    ),
                    benefits = listOf(
                        "Excellent compound movement for the triceps, chest, and anterior deltoids.",
                        "Builds functional upper-body strength and performance."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Skull Crushers",
                    videoResId = R.raw.skull_crushers_video, // Replace with skull_crushers_video
                    instructions = listOf(
                        "Lie on a bench with a barbell or EZ-bar, holding it with a shoulder-width grip.",
                        "Extend your arms straight up so the bar is directly over your shoulders.",
                        "Keep your upper arms stationary and bend only at the elbows to lower the bar towards your forehead.",
                        "Lower the weight until the bar is just above or slightly behind your head.",
                        "Extend your elbows to push the bar back up to the starting position."
                    ),
                    benefits = listOf(
                        "Targets all three heads of the triceps for complete development.",
                        "Increases triceps mass and overall arm size."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("arms", "medium", duration) {
                            navController.popBackStack()
                        }
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
fun IntermediateArmsWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateArmsWorkoutsScreen(navController = rememberNavController())
    }
}
