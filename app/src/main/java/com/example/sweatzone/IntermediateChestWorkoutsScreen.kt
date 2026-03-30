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
fun IntermediateChestWorkoutsScreen(navController: NavController) {
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
                        painter = painterResource(id = R.drawable.chestimg),
                        contentDescription = "Chest Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Chest",
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
                    title = "Dumbbell Bench Press",
                    videoResId = R.raw.dumbbell_bench_press_video,
                    instructions = listOf(
                        "Lie on a flat bench with a dumbbell in each hand.",
                        "Hold the dumbbells at the sides of your chest, palms facing forward.",
                        "Press the weights up until your arms are fully extended.",
                        "Slowly lower the dumbbells back to the starting position."
                    ),
                    benefits = listOf(
                        "Builds chest mass and strength.",
                        "Improves stabilizer muscles more than barbell."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Incline Dumbbell Press",
                    videoResId = R.raw.incline_bench_video,
                    instructions = listOf(
                        "Lie on an incline bench (30-45 degrees).",
                        "Hold dumbbells at your upper chest, palms forward.",
                        "Press the dumbbells up and slightly back.",
                        "Lower the weights slowly and with control."
                    ),
                    benefits = listOf(
                        "Targets the upper pectoral muscles.",
                        "Builds shoulder and tricep strength."
                    )
                )
            }

            // 👇 ADDED MISSING EXERCISES BELOW

            item {
                ExerciseItem(
                    title = "Dumbbell Flys",
                    videoResId = R.raw.dumbbell_fly_video, // Replace with actual dumbbell_flys video
                    instructions = listOf(
                        "Lie on a flat bench holding dumbbells with a neutral grip.",
                        "Start with the dumbbells directly over your chest, elbows slightly bent.",
                        "Lower the weights in a wide arc until you feel a stretch in your chest.",
                        "Bring the dumbbells back to the starting position using your chest muscles."
                    ),
                    benefits = listOf(
                        "Isolates the pectoral muscles for a great stretch and contraction.",
                        "Improves the width and definition of the chest."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Cable Crossovers",
                    videoResId = R.raw.cable_crossover_video, // Replace with actual cable_crossovers video
                    instructions = listOf(
                        "Set pulleys to a high position and grab the handles.",
                        "Stand in the center, take a step forward, and lean slightly.",
                        "With arms extended, pull the handles down and across your body.",
                        "Squeeze your chest at the peak contraction before returning slowly."
                    ),
                    benefits = listOf(
                        "Provides constant tension on the chest muscles.",
                        "Excellent for targeting the inner and lower pecs."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Dumbbell Pullover",
                    videoResId = R.raw.dumbbell_pullover_video, // Replace with actual pullover video
                    instructions = listOf(
                        "Lie with your upper back across a flat bench.",
                        "Hold one dumbbell with both hands, forming a diamond shape.",
                        "Start with the dumbbell over your chest, arms slightly bent.",
                        "Lower the weight behind your head, feeling a stretch in your lats and chest.",
                        "Pull the dumbbell back over your chest to the start."
                    ),
                    benefits = listOf(
                        "Works both the chest and the latissimus dorsi (back muscles).",
                        "Helps expand the rib cage and improves flexibility."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("chest", "medium", duration) {
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
fun IntermediateChestWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateChestWorkoutsScreen(navController = rememberNavController())
    }
}
