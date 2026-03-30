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
fun IntermediateAbsWorkoutsScreen(navController: NavController) {
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
                        painter = painterResource(id = R.drawable.absimg),
                        contentDescription = "Abs Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Abs",
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
                    title = "Hanging Knee Raises",
                    videoResId = R.raw.hanging_leg_raises_video, // Ensure this video exists
                    instructions = listOf(
                        "Hang from a pull-up bar with an overhand grip.",
                        "Keeping your legs straight, raise them until they are parallel to the floor.",
                        "Slowly lower your legs back down."
                    ),
                    benefits = listOf(
                        "Excellent for lower abs and grip strength.",
                        "Decompresses the spine."
                    )
                )
            }

            // 👇 ADDED MISSING EXERCISES BELOW

            item {
                ExerciseItem(
                    title = "Cable Crunches",
                    videoResId = R.raw.cable_crunches_video, // Replace with cable_crunches_video
                    instructions = listOf(
                        "Kneel below a high pulley with a rope attachment.",
                        "Grasp the rope and pull it down until your hands are next to your face.",
                        "Flex your hips slightly and allow the weight to hyperextend your lower back.",
                        "Keeping your hips stationary, crunch your upper body down towards your knees.",
                        "Squeeze your abs hard at the bottom and slowly return to the start."
                    ),
                    benefits = listOf(
                        "Allows you to add weight for progressive overload on your abs.",
                        "Provides constant tension throughout the entire movement."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Russian Twists",
                    videoResId = R.raw.russian_twists_video, // Replace with russian_twists_video
                    instructions = listOf(
                        "Sit on the floor with your knees bent and feet slightly off the ground.",
                        "Lean back to a 45-degree angle, keeping your back straight.",
                        "Hold a weight or clasp your hands together in front of you.",
                        "Twist your torso from side to side, touching the weight to the floor on each side.",
                        "Keep your core engaged throughout."
                    ),
                    benefits = listOf(
                        "Targets the obliques for a stronger, more defined waistline.",
                        "Improves rotational strength and core stability."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Side Plank",
                    videoResId = R.raw.side_plank_video, // Replace with side_plank_video
                    instructions = listOf(
                        "Lie on your side with your legs straight and stacked.",
                        "Prop your upper body up on your forearm, ensuring your elbow is directly under your shoulder.",
                        "Lift your hips off the floor until your body forms a straight line from head to heels.",
                        "Hold this position for the desired amount of time, then switch sides."
                    ),
                    benefits = listOf(
                        "Strengthens the obliques, lower back, and shoulder stabilizers.",
                        "Improves anti-rotational core strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Reverse Crunches",
                    videoResId = R.raw.reverse_crunches_video, // Replace with reverse_crunches_video
                    instructions = listOf(
                        "Lie on your back with your hands by your sides or under your lower back for support.",
                        "Bring your knees towards your chest until they are bent at a 90-degree angle.",
                        "Using your lower abs, lift your hips off the floor and bring your knees towards your chest.",
                        "Slowly lower your hips back down to the starting position."
                    ),
                    benefits = listOf(
                        "Effectively targets the often hard-to-reach lower abdominals.",
                        "Less strain on the neck and spine compared to traditional crunches."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("abs", "medium", duration) {
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
fun IntermediateAbsWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateAbsWorkoutsScreen(navController = rememberNavController())
    }
}

