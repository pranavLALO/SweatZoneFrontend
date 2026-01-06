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
fun AdvanceLegsWorkoutsScreen(navController: NavController) {
    val pinkBg = Color(0xFFFFF0F5)

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController, homeRoute = Screen.AdvanceHome.route)
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
                    title = "Pistol Squats",
                    videoResId = R.raw.pistol_squats_video, // Ensure this video exists
                    instructions = listOf(
                        "Stand on one leg, with the other leg extended out in front of you.",
                        "Squat down on your standing leg until your hamstring is on your calf.",
                        "Keep your balance and drive back up to the starting position.",
                        "This is a highly advanced movement requiring great balance and strength."
                    ),
                    benefits = listOf(
                        "Ultimate single-leg strength and stability test.",
                        "Dramatically improves balance and coordination."
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AdvanceLegsWorkoutsScreenPreview() {
    SweatzoneTheme {
        AdvanceLegsWorkoutsScreen(navController = rememberNavController())
    }
}
