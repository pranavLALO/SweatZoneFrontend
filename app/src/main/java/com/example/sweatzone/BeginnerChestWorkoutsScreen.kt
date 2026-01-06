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
import com.example.sweatzone.ui.components.ExerciseItem // Import the shared, crash-free component

@Composable
fun BeginnerChestWorkoutsScreen(navController: NavController) {
    val lavenderBg = Color(0xFFF3E5F5)

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(lavenderBg)
                .padding(innerPadding)
        ) {
            // Header Image
            item {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.chestimg),
                        contentDescription = "Chest Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
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

            // Exercise Items - Using the shared ExerciseItem to prevent crashes
            item { 
                ExerciseItem(
                    title = "Push Up",
                    videoResId = R.raw.pushup_video,
                    instructions = listOf("Get on the floor on all fours.", "Straighten arms and legs.", "Lower chest to floor.", "Push back up."),
                    benefits = listOf("Strengthens chest.", "Targets core.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Incline Push Up",
                    videoResId = R.raw.incline_pushup_video,
                    instructions = listOf("Hands on bench.", "Lower chest to edge.", "Push back up."),
                    benefits = listOf("Easier on shoulders.", "Targets lower chest.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Knee Push Up",
                    videoResId = R.raw.knee_pushup_video,
                    instructions = listOf("Knees on floor.", "Lower chest.", "Push up."),
                    benefits = listOf("Good for beginners.", "Builds strength.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Barbell Bench Press",
                    videoResId = R.raw.bench_press_video,
                    instructions = listOf("Lie on bench.", "Lower bar to chest.", "Press up."),
                    benefits = listOf("Mass builder.", "Full chest activation.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Incline Bench Press",
                    videoResId = R.raw.incline_bench_video,
                    instructions = listOf("Set bench to 30 degrees.", "Press weight up.", "Lower slowly."),
                    benefits = listOf("Upper chest focus.", "Shoulder strength.")
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
