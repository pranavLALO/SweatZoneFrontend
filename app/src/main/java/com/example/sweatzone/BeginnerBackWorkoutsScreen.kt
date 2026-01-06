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
fun BeginnerBackWorkoutsScreen(navController: NavController) {
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
                        painter = painterResource(id = R.drawable.backimg),
                        contentDescription = "Back Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Back",
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
                    title = "Pull-ups (Assisted)",
                    videoResId = R.raw.pullups_video, // Ensure this video exists in res/raw
                    instructions = listOf(
                        "Use an assisted pull-up machine or a resistance band.",
                        "Grasp the bar with a wide overhand grip.",
                        "Pull your body up until your chin is over the bar.",
                        "Slowly lower yourself back to the starting position."
                    ),
                    benefits = listOf(
                        "Builds a wide back.",
                        "Strengthens biceps and grip."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Lat Pulldowns",
                    videoResId = R.raw.lat_pulldowns_video, // Ensure this video exists in res/raw
                    instructions = listOf(
                        "Sit at a lat pulldown machine and secure your knees.",
                        "Grasp the bar with a wide grip.",
                        "Pull the bar down to your chest, squeezing your back muscles.",
                        "Slowly return the bar to the starting position."
                    ),
                    benefits = listOf(
                        "Excellent for targeting the latissimus dorsi.",
                        "Good for beginners to build back strength."
                    )
                )
            }

            item {
                ExerciseItem(
                    title = "Dumbbell Rows",
                    videoResId = R.raw.dumbbell_rows_video, // Ensure this video exists in res/raw
                    instructions = listOf(
                        "Place one knee and hand on a bench.",
                        "Hold a dumbbell in the opposite hand with your arm extended.",
                        "Pull the dumbbell up to your chest, keeping your back straight.",
                        "Lower the dumbbell slowly."
                    ),
                    benefits = listOf(
                        "Works each side of the back independently.",
                        "Improves core stability."
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeginnerBackWorkoutsScreenPreview() {
    SweatzoneTheme {
        BeginnerBackWorkoutsScreen(navController = rememberNavController())
    }
}
