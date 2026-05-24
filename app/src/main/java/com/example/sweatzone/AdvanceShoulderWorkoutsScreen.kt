package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AdvancedWorkoutTemplate

@Composable
fun AdvanceShoulderWorkoutsScreen(navController: NavController, userViewModel: UserViewModel) {
    AdvancedWorkoutTemplate(
        navController = navController,
        muscleGroup = "shoulder",
        headerResId = R.drawable.shoulderimg,
        title = "Shoulder",
        userViewModel = userViewModel
    )
}
