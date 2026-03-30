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
fun StrengthTrainingMaleScreen(navController: NavController) {
    val palePinkBg = Color(0xFFFFF0F5)

    Scaffold(
        bottomBar = {
            // This screen is part of onboarding, so the main AppBottomNavBar might not be appropriate
            // Or it could navigate to the next step
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(palePinkBg)
                .padding(innerPadding)
        ) {
            // --- Header Image ---
            item {
                Box(modifier = Modifier.fillMaxWidth().height(180.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.strength_training_banner), // You need to add this drawable
                        contentDescription = "Strength Training",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = "Goal",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(8.dp)) }

            // --- Exercises ---

            item { ExerciseItem(
                title = "Barbell Squats",
                videoResId = R.raw.squats_video, // Ensure videos are in res/raw
                instructions = listOf("Rest the barbell on your upper back.", "Lower your hips back and down, keeping your chest up.", "Go until your thighs are parallel to the floor, then drive back up."),
                benefits = listOf("Builds overall leg mass and strength.", "Increases core stability and hormone release.")
            )}

            item { ExerciseItem(
                title = "Bench Press",
                videoResId = R.raw.bench_press_video,
                instructions = listOf("Lie on a flat bench, grab the bar with a medium grip.", "Lower the bar to your mid-chest.", "Push the bar back up until your arms are fully extended."),
                benefits = listOf("Builds upper body pushing strength.", "Targets chest, shoulders, and triceps.")
            )}

            item { ExerciseItem(
                title = "Bent-Over Rows",
                videoResId = R.raw.barbell_rows_video,
                instructions = listOf("Hold a barbell with a wide grip, bend at your hips.", "Keep your back straight and pull the bar towards your lower chest.", "Squeeze your back muscles at the top."),
                benefits = listOf("Builds a thick and strong back.", "Improves posture and pulling strength.")
            )}

            item { ExerciseItem(
                title = "Overhead Press",
                videoResId = R.raw.overhead_press_video,
                instructions = listOf("Start with a barbell at your shoulders.", "Press the bar directly overhead until your arms are locked out.", "Lower the bar back to your shoulders with control."),
                benefits = listOf("Builds strong and stable shoulders.", "Excellent for overall upper body power.")
            )}

            item { ExerciseItem(
                title = "Bicep Curls",
                videoResId = R.raw.bicep_curls_video,
                instructions = listOf("Stand holding dumbbells at your sides, palms facing forward.", "Curl the weights up towards your shoulders.", "Squeeze the biceps at the top and lower slowly."),
                benefits = listOf("Isolates and builds the bicep muscles for bigger arms.", "Improves grip strength.")
            )}

            item { ExerciseItem(
                title = "Tricep Dips",
                videoResId = R.raw.tricep_extension_video,
                instructions = listOf("Use parallel bars or a sturdy bench.", "Lower your body until your elbows are at a 90-degree angle.", "Push back up to the starting position."),
                benefits = listOf("Builds tricep mass and lockout strength.", "Great bodyweight exercise for arms.")
            )}

             item { Spacer(modifier = Modifier.height(24.dp)) }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun StrengthTrainingMaleScreenPreview() {
    SweatzoneTheme {
        StrengthTrainingMaleScreen(navController = rememberNavController())
    }
}
