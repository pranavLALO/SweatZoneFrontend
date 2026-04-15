package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AdvancedWorkoutTemplate

@Composable
fun AdvanceAbsWorkoutsScreen(navController: NavController) {
    AdvancedWorkoutTemplate(
        navController = navController,
        muscleGroup = "abs",
        headerResId = R.drawable.absimg,
        title = "Abs"
    )
}
