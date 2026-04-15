package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AdvancedWorkoutTemplate

@Composable
fun AdvanceChestWorkoutsScreen(navController: NavController) {
    AdvancedWorkoutTemplate(
        navController = navController,
        muscleGroup = "chest",
        headerResId = R.drawable.chestimg,
        title = "Chest"
    )
}