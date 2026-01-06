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
fun BeginnerAbsWorkoutsScreen(navController: NavController) {
    val pinkBg = Color(0xFFFFF0F5)

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
                    title = "Crunches",
                    videoResId = R.raw.crunches_video, // Ensure this video exists in res/raw
                    instructions = listOf(
                        "Lie flat on your back, knees bent, feet on the floor.",
                        "Place hands behind your head or across your chest.",
                        "Lift your upper body towards your knees, contracting your abs.",
                        "Slowly lower back down."
                    ),
                    benefits = listOf(
                        "Isolates the rectus abdominis.",
                        "Improves core strength and stability."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Leg Raises",
                    videoResId = R.raw.leg_raises_video, // Ensure this video exists in res/raw
                    instructions = listOf(
                        "Lie on your back with legs straight.",
                        "Place hands under your glutes for support.",
                        "Lift your legs towards the ceiling until your hips are off the floor.",
                        "Slowly lower your legs back down without touching the floor."
                    ),
                    benefits = listOf(
                        "Targets the lower abdominal muscles.",
                        "Enhances hip flexor strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Plank",
                    videoResId = R.raw.plank_video, // Ensure this video exists in res/raw
                    instructions = listOf(
                        "Start in a push-up position on your forearms.",
                        "Keep your body in a straight line from head to heels.",
                        "Engage your core and glutes.",
                        "Hold the position for the desired time."
                    ),
                    benefits = listOf(
                        "Strengthens the entire core.",
                        "Improves posture and body stability."
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeginnerAbsWorkoutsScreenPreview() {
    SweatzoneTheme {
        BeginnerAbsWorkoutsScreen(navController = rememberNavController())
    }
}
