package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AdvancedWorkoutTemplate

@Composable
fun AdvanceBackWorkoutsScreen(navController: NavController) {
    AdvancedWorkoutTemplate(
        navController = navController,
        muscleGroup = "back",
        headerResId = R.drawable.backimg,
        title = "Back"
    )
}
