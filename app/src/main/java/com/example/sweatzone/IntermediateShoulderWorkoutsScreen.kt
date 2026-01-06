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
fun IntermediateShoulderWorkoutsScreen(navController: NavController) {
    val pinkBg = Color(0xFFFFF0F5)

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController, homeRoute = Screen.IntermediateHome.route)
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
                        painter = painterResource(id = R.drawable.shoulderimg),
                        contentDescription = "Shoulder Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
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

            // Exercises
            item {
                ExerciseItem(
                    title = "Overhead Press (Barbell)",
                    videoResId = R.raw.overhead_press_video, // Ensure this video exists
                    instructions = listOf(
                        "Set a barbell at shoulder height on a rack.",
                        "Grip the bar slightly wider than shoulder-width.",
                        "Press the bar directly overhead until your arms are fully locked out.",
                        "Lower the bar back to your shoulders under control."
                    ),
                    benefits = listOf(
                        "Builds overall shoulder mass and strength.",
                        "Excellent compound movement for upper body power."
                    )
                )
            }

            // 👇 ADDED MISSING EXERCISES BELOW
            item {
                ExerciseItem(
                    title = "Arnold Press",
                    videoResId = R.raw.arnold_press_video, // Replace with arnold_press_video
                    instructions = listOf(
                        "Sit on a bench holding dumbbells in front of your shoulders, palms facing you.",
                        "As you press up, rotate your hands so your palms face forward at the top.",
                        "Reverse the motion as you lower the dumbbells back to the start.",
                        "This combines a press and a fly-like motion."
                    ),
                    benefits = listOf(
                        "Hits all three heads of the deltoid.",
                        "Increases time under tension for muscle growth."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Lateral Raises",
                    videoResId = R.raw.lateral_raises_video, // Replace with lateral_raises_video
                    instructions = listOf(
                        "Stand with dumbbells at your sides, palms facing in.",
                        "Keeping a slight bend in your elbows, raise your arms out to the sides until they are parallel to the floor.",
                        "Focus on using your side delts, not momentum.",
                        "Slowly lower the dumbbells back to the starting position."
                    ),
                    benefits = listOf(
                        "Isolates the medial (side) deltoid, creating shoulder width.",
                        "Improves the 'V-taper' aesthetic."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Front Raises",
                    videoResId = R.raw.front_raises_video, // Replace with front_raises_video
                    instructions = listOf(
                        "Hold dumbbells in front of your thighs, palms facing your body.",
                        "Raise one dumbbell straight out in front of you until it's at shoulder level.",
                        "Lower it with control and repeat with the other arm.",
                        "Avoid swinging or using your back to lift the weight."
                    ),
                    benefits = listOf(
                        "Targets the anterior (front) deltoid.",
                        "Helps balance shoulder development."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Face Pulls",
                    videoResId = R.raw.face_pulls_video, // Replace with face_pulls_video
                    instructions = listOf(
                        "Set a cable rope attachment at face level.",
                        "Grip the rope with both hands, palms facing each other.",
                        "Pull the rope towards your face, leading with your hands and elbows high.",
                        "Squeeze your rear delts and upper back at the end of the motion."
                    ),
                    benefits = listOf(
                        "Excellent for rear deltoid and upper back development.",
                        "Improves posture and shoulder health."
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntermediateShoulderWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateShoulderWorkoutsScreen(navController = rememberNavController())
    }
}
