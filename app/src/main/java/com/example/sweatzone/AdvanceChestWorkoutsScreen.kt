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
fun AdvanceChestWorkoutsScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()

    // Elegant pale pink background
    val pinkBg = Color(0xFFFFF0F5)
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }

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
                        painter = painterResource(id = R.drawable.chestimg), // Using existing chest image
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

            // --- Exercise 1: Butterfly (pec deck) ---
            item {
                ExerciseItem(
                    title = "Butterfly (pec deck)",
                    videoResId = R.raw.butterfly_pec_deck_video, // Create in res/raw
                    instructions = listOf(
                        "Sit on the machine with your back flat on the pad.",
                        "Adjust the seat so the handles are at chest level.",
                        "Place your forearms on the padded levers (if using pec deck) or grab handles with arms parallel to floor.",
                        "Push the levers together while you squeeze your chest muscles.",
                        "Return to the starting position slowly, keeping tension on your chest."
                    ),
                    benefits = listOf(
                        "Effectively isolates the pectoral muscles for targeted growth.",
                        "Limit the involvement of shoulder and triceps muscles.",
                        "Controlled movement reduces the risk of shoulder injury compared to free weights.",
                        "Easy to learn form making it suitable for beginners."
                    )
                )
            }

            // --- Exercise 2: Decline Bench Press ---
            item {
                ExerciseItem(
                    title = "Decline Bench Press",
                    videoResId = R.raw.decline_bench_press_video, // Create in res/raw
                    instructions = listOf(
                        "Lie on a decline bench with your feet securely locked under the leg brace.",
                        "Grasp the barbell with a medium width grip, wider than shoulder width.",
                        "Unrack the barbell and hold it straight over your torso above with your arms locked.",
                        "Lower the bar slowly to your lower chest, keeping your elbows tucked in at a 45-degree angle.",
                        "Push the barbell back up to the starting position until your arms are fully extended."
                    ),
                    benefits = listOf(
                        "Primarily targets the lower pectoral (chest) muscles.",
                        "Engages the anterior deltoids (front shoulders) and triceps as secondary muscles.",
                        "Allows for lifting heavier weight compared to flat or incline bench press.",
                        "Reduces stress on the lower back and shoulders compared to other bench variations."
                    )
                )
            }

            // --- Exercise 3: Wide Grip Bench Press ---
            item {
                ExerciseItem(
                    title = "Wide Grip Bench Press",
                    videoResId = R.raw.wide_grip_bench_video, // Create in res/raw
                    instructions = listOf(
                        "Lie flat on a bench and position your hands on the barbell significantly wider than shoulder-width apart.",
                        "Unrack the bar and hold it straight over your sternum with arms locked.",
                        "Lower the bar slowly to your chest, flaring your elbows slightly out.",
                        "Pause for a second when the bar touches your chest.",
                        "Press the bar back up forcefully to full extension."
                    ),
                    benefits = listOf(
                        "Targets the pectoral muscles more intensely than standard press.",
                        "Shifts focus away from triceps, putting more load on the chest.",
                        "Increases upper body pressing power.",
                        "Activates outer chest fibers for a wider chest look."
                    )
                )
            }

            // --- Exercise 4: Chest Fly (Dumbbell) ---
            item {
                ExerciseItem(
                    title = "Chest Fly (Dumbbell)",
                    videoResId = R.raw.dumbbell_fly_video, // Create in res/raw
                    instructions = listOf(
                        "Lie on a flat bench holding a dumbbell in each hand, palms facing each other.",
                        "Start with arms extended directly above your chest with a slight bend in your elbows.",
                        "Slowly lower the weights out to your sides in a wide arc until you feel a stretch in your chest.",
                        "Pause briefly at the bottom.",
                        "Squeeze your chest muscles to bring the dumbbells back up to the starting position in the same arc motion."
                    ),
                    benefits = listOf(
                        "Effectively isolates and strengthens the pectoral muscles.",
                        "Improves flexibility and range of motion in the chest.",
                        "Helps to widen the look of the muscular appearance.",
                        "Engages stabilizer muscles in the shoulders and back."
                    )
                )
            }

            // --- Exercise 5: Low Cable Fly Crossovers ---
            item {
                ExerciseItem(
                    title = "Low Cable Fly Crossovers",
                    videoResId = R.raw.low_cable_fly_video, // Create in res/raw
                    instructions = listOf(
                        "Set the pulleys to the lowest setting on the cable machine.",
                        "Stand in the middle of the machine, grabbing a handle in each hand.",
                        "Step forward to create tension, with your arms extended low at your sides.",
                        "With a slight bend in your elbows, bring your hands together in an upward arc in front of your chest.",
                        "Squeeze your chest muscles at the peak of the movement, then slowly return to the starting position."
                    ),
                    benefits = listOf(
                        "Targets the upper pectoral muscles, giving the chest a full, uplifted look.",
                        "Provides constant tension throughout the entire range of motion.",
                        "Improves chest sculpting, definition and detail in the center line.",
                        "Allows for a good stretch and full contraction of the pectoral muscles."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("chest", "high", duration) {
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