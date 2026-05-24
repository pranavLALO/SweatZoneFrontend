package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AdvancedWorkoutTemplate

@Composable
fun AdvanceArmsWorkoutsScreen(navController: NavController, userViewModel: UserViewModel) {
    AdvancedWorkoutTemplate(
        navController = navController,
        muscleGroup = "arms",
        headerResId = R.drawable.armsimg,
        title = "Arms",
        userViewModel = userViewModel
    )
}