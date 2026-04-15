package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AdvancedWorkoutTemplate

@Composable
fun AdvanceLegsWorkoutsScreen(navController: NavController) {
    AdvancedWorkoutTemplate(
        navController = navController,
        muscleGroup = "legs",
        headerResId = R.drawable.legimg,
        title = "Legs"
    )
}
