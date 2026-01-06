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
fun LowerBodyHomeWorkoutScreen(navController: NavController) {
    // Elegant pale pink background
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
            // --- Header Image ---
            item {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.legimg), // Using leg image for Lower Body
                        contentDescription = "Lower Body Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Lower Body",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            // --- Exercise 1: Bodyweight Squat ---
            item {
                ExerciseItem(
                    title = "Bodyweight Squat",
                    videoResId = R.raw.squats_video, 
                    instructions = listOf(
                        "Stand with your feet shoulder-width apart, toes slightly turned out.",
                        "Lower your hips back and down as if sitting in a chair, keeping your chest up.",
                        "Lower until your thighs are at least parallel to the floor.",
                        "Push through your heels to return to the starting position, squeezing your glutes at the top.",
                        "Repeat for the desired number of repetitions."
                    ),
                    benefits = listOf(
                        "Strengthens quadriceps, hamstrings, and glutes.",
                        "Improves core stability and balance.",
                        "Functional movement that mimics daily activities."
                    )
                )
            }

            // --- Exercise 2: Forward Lunges ---
            item {
                ExerciseItem(
                    title = "Forward Lunges",
                    videoResId = R.raw.walking_lunges_video,
                    instructions = listOf(
                        "Stand tall with feet hip-width apart.",
                        "Take a large step forward with one leg and lower your hips until both knees are bent at a 90-degree angle.",
                        "Ensure your front knee is directly above your ankle and your back knee is just above the floor.",
                        "Push off your front foot to return to the starting position.",
                        "Repeat on the other leg."
                    ),
                    benefits = listOf(
                        "Builds unilateral leg strength and balance.",
                        "Targets glutes, quads, and hamstrings.",
                        "Improves hip flexibility."
                    )
                )
            }

            // --- Exercise 3: Glute Bridge ---
            item {
                ExerciseItem(
                    title = "Glute Bridge",
                    videoResId = R.raw.glute_bridge_video, 
                    instructions = listOf(
                        "Lie on your back with your knees bent and feet flat on the floor.",
                        "Place your arms at your sides with palms facing down.",
                        "Lift your hips off the floor until your knees, hips, and shoulders form a straight line.",
                        "Squeeze your glutes hard at the top and hold for a few seconds.",
                        "Slowly lower your hips back to the starting position."
                    ),
                    benefits = listOf(
                        "Targets and strengthens the glutes and hamstrings.",
                        "Improves core stability.",
                        "Helps alleviate lower back pain."
                    )
                )
            }

            // --- Exercise 4: Plank ---
            item {
                ExerciseItem(
                    title = "Plank",
                    videoResId = R.raw.plank_video, 
                    instructions = listOf(
                        "Start in a push-up position but with your weight on your forearms instead of your hands.",
                        "Keep your body in a straight line from your head to your heels.",
                        "Engage your core by sucking your belly button in towards your spine.",
                        "Hold this position without letting your hips sag or arching your back."
                    ),
                    benefits = listOf(
                        "Builds deep core strength and stability.",
                        "Improves posture and balance.",
                        "Strengthens shoulders, chest, and back."
                    )
                )
            }

            // --- Exercise 5: Sumo Squat ---
            item {
                ExerciseItem(
                    title = "Sumo Squat",
                    videoResId = R.raw.sumo_squat_video, 
                    instructions = listOf(
                        "Stand with feet wider than shoulder-width apart, toes pointed out at a 45-degree angle.",
                        "Lower your hips back and down, keeping your chest up and knees tracking over your toes.",
                        "Go as deep as comfortable, ideally until thighs are parallel to the floor.",
                        "Drive back up through your heels, squeezing glutes."
                    ),
                    benefits = listOf(
                        "Targets inner thighs (adductors) and glutes more than standard squats.",
                        "Improves hip flexibility.",
                        "Builds lower body strength."
                    )
                )
            }

            // --- Exercise 6: Jump Squat ---
            item {
                ExerciseItem(
                    title = "Jump Squat",
                    videoResId = R.raw.jump_squat_video,
                    instructions = listOf(
                        "Perform a standard squat by lowering your hips back and down.",
                        "Explosively jump up as high as you can, extending your hips, knees, and ankles.",
                        "Land softly on the balls of your feet and immediately transition into the next squat.",
                        "Use your arms to generate momentum."
                    ),
                    benefits = listOf(
                        "Builds explosive power and cardiovascular endurance.",
                        "Increases heart rate for fat burning.",
                        "Tones legs and glutes."
                    )
                )
            }

            // --- Exercise 7: Single-Leg Glute Bridge ---
            item {
                ExerciseItem(
                    title = "Single-Leg Glute Bridge",
                    videoResId = R.raw.single_leg_glute_bridge_video, 
                    instructions = listOf(
                        "Lie on your back with one knee bent and foot flat on the floor.",
                        "Extend the other leg straight up towards the ceiling.",
                        "Lift your hips off the floor by driving through the heel of the grounded foot.",
                        "Squeeze your glute at the top, then lower back down slowly.",
                        "Repeat on one side before switching to the other."
                    ),
                    benefits = listOf(
                        "Isolates the glutes and hamstrings on one side.",
                        "Corrects muscle imbalances.",
                        "Increases core stability and hip strength."
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LowerBodyHomeWorkoutScreenPreview() {
    SweatzoneTheme {
        LowerBodyHomeWorkoutScreen(navController = rememberNavController())
    }
}
