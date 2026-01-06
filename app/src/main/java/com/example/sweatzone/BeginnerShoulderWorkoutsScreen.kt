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
fun BeginnerShoulderWorkoutsScreen(navController: NavController) {
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
                        painter = painterResource(id = R.drawable.shoulderimg),
                        contentDescription = "Shoulder Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Shoulder",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            // Exercise Items
            item { 
                ExerciseItem(
                    title = "Dumbbell Shoulder Press",
                    videoResId = R.raw.shoulder_press_video,
                    instructions = listOf("Sit on a bench with back support.", "Hold dumbbells at shoulder height.", "Press weights up until arms are extended.", "Lower back to start."),
                    benefits = listOf("Targets anterior and lateral deltoids.", "Engages triceps and upper chest.", "Improves overhead stability.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Front Raises",
                    videoResId = R.raw.front_raises_video,
                    instructions = listOf("Stand with dumbbells in front of thighs.", "Lift weights to shoulder height.", "Lower slowly."),
                    benefits = listOf("Isolates anterior deltoid.", "Improves shoulder flexion.", "Strengthens upper chest.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Lateral Raises",
                    videoResId = R.raw.lateral_raises_video,
                    instructions = listOf("Stand with dumbbells at sides.", "Raise arms out to sides until shoulder height.", "Lower slowly."),
                    benefits = listOf("Isolates lateral deltoid.", "Builds shoulder width.", "Improves shoulder stability.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Seated Arnold Press",
                    videoResId = R.raw.arnold_press_video,
                    instructions = listOf("Start with dumbbells in front of chest, palms facing you.", "Press up while rotating palms forward.", "Reverse motion on way down."),
                    benefits = listOf("Targets all three deltoid heads.", "Increases range of motion.", "Engages stabilizer muscles.")
                )
            }

            item { 
                ExerciseItem(
                    title = "Face Pulls (rope)",
                    videoResId = R.raw.face_pulls_video,
                    instructions = listOf("Attach rope to high pulley.", "Pull rope towards face, separating hands.", "Squeeze rear delts."),
                    benefits = listOf("Targets rear deltoids and rotator cuff.", "Improves posture.", "Balances shoulder strength.")
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
