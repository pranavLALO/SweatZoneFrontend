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
fun IntermediateLegsWorkoutsScreen(navController: NavController) {
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
                        painter = painterResource(id = R.drawable.legimg),
                        contentDescription = "Legs Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Legs",
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
                    title = "Barbell Squats",
                    videoResId = R.raw.barbell_squat_video, // Ensure this video exists
                    instructions = listOf(
                        "Set a barbell on a rack at shoulder height.",
                        "Step under the bar and rest it on your upper back.",
                        "Lift the bar off the rack and take a step back.",
                        "Squat down until your thighs are parallel to the floor.",
                        "Drive back up to the starting position."
                    ),
                    benefits = listOf(
                        "The king of leg exercises for overall mass and strength.",
                        "Engages the entire lower body and core."
                    )
                )
            }

            // 👇 ADDED MISSING EXERCISES BELOW

            item {
                ExerciseItem(
                    title = "Romanian Deadlifts",
                    videoResId = R.raw.romanian_deadlift_video, // Replace with rdl_video
                    instructions = listOf(
                        "Hold a barbell with an overhand grip, hands just outside your thighs.",
                        "Keeping your legs almost straight (slight knee bend), hinge at your hips.",
                        "Lower the bar by pushing your hips back, keeping it close to your legs.",
                        "Go down until you feel a deep stretch in your hamstrings.",
                        "Return to the start by driving your hips forward and squeezing your glutes."
                    ),
                    benefits = listOf(
                        "Excellent for hamstring and glute development.",
                        "Improves hip mobility and posterior chain strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Bulgarian Split Squats",
                    videoResId = R.raw.bulgarian_split_squat_video, // Replace with split_squat_video
                    instructions = listOf(
                        "Place the top of your back foot on a bench behind you.",
                        "Hold dumbbells in each hand and keep your torso upright.",
                        "Lower your hips until your front thigh is parallel to the ground.",
                        "Ensure your front knee does not travel past your toes.",
                        "Push through your front foot to return to the starting position."
                    ),
                    benefits = listOf(
                        "Great for isolating each leg, correcting imbalances.",
                        "Builds quad, glute, and hamstring strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Leg Press",
                    videoResId = R.raw.leg_press_video, // Replace with leg_press_video
                    instructions = listOf(
                        "Sit on the leg press machine and place your feet shoulder-width apart on the platform.",
                        "Push the platform away until your legs are fully extended, but not locked.",
                        "Slowly lower the weight until your knees are at a 90-degree angle.",
                        "Press the weight back up to the starting position.",
                        "Do not let your lower back round off the pad."
                    ),
                    benefits = listOf(
                        "Allows for heavy weight with less stress on the lower back.",
                        "Effectively targets the quads, glutes, and hamstrings."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Seated Hamstring Curl",
                    videoResId = R.raw.hamstring_curl_video, // Replace with hamstring_curl_video
                    instructions = listOf(
                        "Sit on the machine and adjust the pads to fit securely.",
                        "The lower leg pad should rest just above your ankles.",
                        "Curl your legs down as far as possible, squeezing your hamstrings.",
                        "Hold the contraction for a moment at the bottom.",
                        "Slowly return the weight to the starting position."
                    ),
                    benefits = listOf(
                        "Isolates the hamstring muscles for targeted growth.",
                        "Strengthens the knee joint."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Standing Calf Raises",
                    videoResId = R.raw.calf_raises_video, // Replace with calf_raise_video
                    instructions = listOf(
                        "Stand on a calf raise machine or a step with the balls of your feet.",
                        "Let your heels hang off the edge to get a good stretch.",
                        "Push through the balls of your feet to raise your heels as high as possible.",
                        "Squeeze your calves at the top.",
                        "Slowly lower your heels back down past the edge of the step."
                    ),
                    benefits = listOf(
                        "Directly targets the gastrocnemius muscle for calf size.",
                        "Improves ankle stability and explosive power for running and jumping."
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IntermediateLegsWorkoutsScreenPreview() {
    SweatzoneTheme {
        IntermediateLegsWorkoutsScreen(navController = rememberNavController())
    }
}
