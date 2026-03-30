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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.components.ExerciseItem

@Composable
fun AdvanceArmsWorkoutsScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }
    val pinkBg = Color(0xFFFFF0F5)

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
                        painter = painterResource(id = R.drawable.armsimg),
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

            // --- Exercises ---
            // IMPORTANT: Make sure you have the video files (e.g., preacher_curl_video.mp4) in your res/raw folder.

            item {
                ExerciseItem(
                    title = "Preacher Curl",
                    videoResId = R.raw.preacher_curl_video,
                    instructions = listOf(
                        "Adjust the seat height so your armpits rest comfortably over the top of the pad.",
                        "Hold the barbell or EZ-bar with an underhand grip, hands about shoulder-width apart.",
                        "With your upper arms flat against the pad and your chest pressed against the support.",
                        "Slowly curl the weight up towards your shoulders, squeezing your biceps at the top.",
                        "Lower the weight back down in a controlled manner until your arms are fully extended."
                    ),
                    benefits = listOf(
                        "Isolates the biceps by preventing body movement and cheating.",
                        "Focuses heavily on the short head of the biceps for peak development.",
                        "Allows for a full range of motion with strict form, reducing injury risk.",
                        "Effective for building strength and definition in the upper arms."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Cable Bayesian Curl",
                    videoResId = R.raw.bayesian_curl_video,
                    instructions = listOf(
                        "Set up a single cable pulley at the lowest position.",
                        "Face away from the machine, holding the handle with one hand, arm extended behind you.",
                        "Step forward slightly to create tension on the cable.",
                        "Curl the handle forward while keeping your elbow behind your torso.",
                        "Squeeze the bicep hard at the peak contraction, then slowly lower back."
                    ),
                    benefits = listOf(
                        "Maximizes the stretch on the long head of the biceps.",
                        "Provides constant tension throughout the entire movement.",
                        "Unique angle stimulates muscle fibers differently than standard curls."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Close Grip Bench Press",
                    videoResId = R.raw.close_grip_bench_video,
                    instructions = listOf(
                        "Lie back on a flat bench. Lift the bar with a close grip (hands shoulder-width or slightly narrower).",
                        "Lower the bar slowly to your lower chest, keeping your elbows tucked close to your torso.",
                        "Push the bar back up explosively to the starting position.",
                        "Focus on using your triceps to drive the weight up rather than your chest."
                    ),
                    benefits = listOf(
                        "One of the best compound movements for building tricep mass and strength.",
                        "Allows for heavier loading compared to isolation exercises.",
                        "Has significant carryover to standard bench press strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Tricep Rope Pushdown",
                    videoResId = R.raw.rope_pushdown_video,
                    instructions = listOf(
                        "Attach a rope to a high pulley cable machine and hold the ends with a neutral grip.",
                        "Stand upright with a slight forward lean, keeping your elbows tucked firmly by your sides.",
                        "Push the rope down until arms are fully extended, pulling the handles apart at the bottom.",
                        "Squeeze your triceps hard, then control the weight back up slowly.",
                        "Maintain strict form; immediately reduce the weight if you use momentum."
                    ),
                    benefits = listOf(
                        "Sharpens isolation on outer triceps head for horseshoe muscle detail.",
                        "The rope allows for a greater range of motion and stronger peak contraction.",
                        "Increases lockout power for pressing movements.",
                        "Constant tension from the cable machine maximizes muscle hypertrophy."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("arms", "high", duration) {
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

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}