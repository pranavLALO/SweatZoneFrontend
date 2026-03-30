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
fun IntermediateBackWorkoutsScreen(navController: NavController) {
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

            // Exercises
            item {
                ExerciseItem(
                    title = "Barbell Rows",
                    videoResId = R.raw.barbell_rows_video, // Ensure this video exists
                    instructions = listOf(
                        "Stand with your feet shoulder-width apart.",
                        "Bend at your hips and knees and grab a barbell with an overhand grip.",
                        "Pull the barbell to your upper waist, squeezing your back muscles.",
                        "Slowly lower the barbell back to the starting position."
                    ),
                    benefits = listOf(
                        "Builds a thick, strong back.",
                        "Excellent compound movement for overall upper body strength."
                    )
                )
            }

            // 👇 ADDED MISSING EXERCISES BELOW
            item {
                ExerciseItem(
                    title = "Pull-Ups",
                    videoResId = R.raw.pullups_video, // Replace with pull_ups_video
                    instructions = listOf(
                        "Grab the pull-up bar with a grip slightly wider than shoulder-width.",
                        "Hang with your arms fully extended, engaging your core.",
                        "Pull your body up until your chin is over the bar.",
                        "Focus on squeezing your back and lats.",
                        "Lower yourself back down with control."
                    ),
                    benefits = listOf(
                        "The ultimate back-width builder.",
                        "Develops lats, biceps, and grip strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "T-Bar Row",
                    videoResId = R.raw.tbar_row_video, // Replace with t_bar_row_video
                    instructions = listOf(
                        "Stand over a loaded T-bar, feet shoulder-width apart.",
                        "Bend at the hips and grab the handles with a neutral grip.",
                        "Keeping your back straight, pull the weight up towards your chest.",
                        "Squeeze your mid-back muscles at the top of the movement."
                    ),
                    benefits = listOf(
                        "Targets the middle back for thickness and density.",
                        "Allows for heavy weight with a stable position."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Seated Cable Row",
                    videoResId = R.raw.cable_crossover_video, // Replace with cable_row_video
                    instructions = listOf(
                        "Sit at a cable row machine with your feet on the platform.",
                        "Grab the handle with a neutral grip, keeping your back straight.",
                        "Pull the handle towards your lower abdomen.",
                        "Squeeze your shoulder blades together and pause briefly.",
                        "Slowly extend your arms back to the starting position."
                    ),
                    benefits = listOf(
                        "Excellent for developing mid-back thickness.",
                        "Provides constant tension and a controlled motion."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Single-Arm Dumbbell Row",
                    videoResId = R.raw.dumbbell_row_video, // Replace with dumbbell_row_video
                    instructions = listOf(
                        "Place one knee and hand on a flat bench for support.",
                        "Hold a dumbbell in the opposite hand with your arm extended.",
                        "Pull the dumbbell up to the side of your chest, keeping your back straight.",
                        "Focus on pulling with your back muscles, not your arm.",
                        "Lower the dumbbell slowly and repeat."
                    ),
                    benefits = listOf(
                        "Corrects strength imbalances between the left and right side.",
                        "Allows for a greater range of motion and deeper muscle contraction."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("back", "medium", duration) {
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
fun IntermediateBackWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateBackWorkoutsScreen(navController = rememberNavController())
    }
}
