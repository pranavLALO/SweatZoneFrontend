package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // <-- CORRECT IMPORT
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
import com.example.sweatzone.ui.components.ExerciseItem // Using the centralized component
import com.example.sweatzone.ui.theme.SweatzoneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAbsWorkoutScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val pinkBg = Color(0xFFFFF0F5)
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Home Abs Workout", fontWeight = FontWeight.Bold) },
                navigationIcon = { 
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(pinkBg)
                .padding(innerPadding)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                ExerciseItem(
                    title = "Crunches",
                    videoResId = 0,
                    instructions = listOf(
                        "Lie on your back with your knees bent and feet flat on the floor.",
                        "Place your hands across your chest or behind your head.",
                        "Contract your abs to lift your upper body off the floor.",
                        "Slowly lower back down."
                    ),
                    benefits = listOf(
                        "Strengthens rectus abdominis.",
                        "Improves core stability."
                    )
                )
            }
            item {
                ExerciseItem(
                    title = "Mountain Climbers",
                    videoResId = 0,
                    instructions = listOf(
                        "Start in a high plank position.",
                        "Quickly bring one knee towards your chest, then return it.",
                        "Alternate legs as if running in place."
                    ),
                    benefits = listOf(
                        "Full-body cardio exercise.",
                        "Builds core strength and agility."
                    )
                )
            }
            item {
                ExerciseItem(
                    title = "Leg Raises",
                    videoResId = 0,
                    instructions = listOf(
                        "Lie on your back with legs straight.",
                        "Raise your legs towards the ceiling until your hips lift off the floor.",
                        "Slowly lower your legs without letting them touch the floor."
                    ),
                    benefits = listOf(
                        "Targets lower abdominal muscles.",
                        "Enhances hip flexor strength."
                    )
                )
            }
            item {
                ExerciseItem(
                    title = "Flutter Kicks",
                    videoResId = 0,
                    instructions = listOf(
                        "Lie on your back, legs straight, hands under your glutes.",
                        "Lift your head and shoulders slightly.",
                        "Kick your legs up and down in a small, rapid motion."
                    ),
                    benefits = listOf(
                        "Excellent for lower abs and hip flexors.",
                        "Builds core endurance."
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

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeAbsWorkoutScreenPreview() {
    SweatzoneTheme {
        HomeAbsWorkoutScreen(navController = rememberNavController())
    }
}
