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
import com.example.sweatzone.ui.components.ExerciseItem
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun StrengthTrainingFemaleScreen(navController: NavController) {
    val palePinkBg = Color(0xFFFFF0F5)

    Scaffold { innerPadding ->
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
                        painter = painterResource(id = R.drawable.strength_training_banner), // Ensure this exists or use a placeholder
                        contentDescription = "Strength Training Female",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Text(
                            text = "Strength Training: Female Focus",
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
                title = "Goblet Squats",
                videoResId = 0,
                instructions = listOf("Hold a weight at chest level.", "Squat down keeping your chest up and back straight.", "Drive back up through your heels."),
                benefits = listOf("Targets quads and glutes.", "Easier on the back than barbell squats.")
            )}

            item { ExerciseItem(
                title = "Romanian Deadlifts",
                videoResId = 0, // Placeholder if deadlift video missing
                instructions = listOf("Hold weights in front of thighs.", "Hinge at hips keeping legs slightly bent.", "Lower weights until you feel a stretch in hamstrings.", "Squeeze glutes to return to standing."),
                benefits = listOf("Strengthens hamstrings and glutes.", "Improves posture.")
            )}

            item { ExerciseItem(
                title = "Glute Bridges",
                videoResId = 0,
                instructions = listOf("Lie on back, knees bent.", "Lift hips until body forms a straight line.", "Squeeze glutes at the top.", "Lower back down."),
                benefits = listOf("Isolates glutes.", "Builds core stability.")
            )}

            item { ExerciseItem(
                title = "Push-Ups (or Knee Push-Ups)",
                videoResId = 0,
                instructions = listOf("Start in plank or on knees.", "Lower chest to floor.", "Push back up keeping core tight."),
                benefits = listOf("Upper body strength (chest, shoulders, triceps).", "Core stability.")
            )}

            item { ExerciseItem(
                title = "Dumbbell Rows",
                videoResId = 0,
                instructions = listOf("Lean on a bench/chair.", "Pull weight up to hip.", "Lower slowly."),
                benefits = listOf("Strengthens back muscles.", "Improves posture.")
            )}

            item { ExerciseItem(
                title = "Plank",
                videoResId = 0,
                instructions = listOf("Hold push-up position on elbows.", "Keep body straight.", "Hold as long as possible."),
                benefits = listOf("Total core strength.", "Stability.")
            )}

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun StrengthTrainingFemaleScreenPreview() {
    SweatzoneTheme {
        StrengthTrainingFemaleScreen(navController = rememberNavController())
    }
}
