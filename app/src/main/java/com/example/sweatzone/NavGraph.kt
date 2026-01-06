package com.example.sweatzone

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavGraph() {
    val navController: NavHostController = rememberNavController()
    val userViewModel: UserViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Onboarding.route
    ) {
        // --- Onboarding & User Setup Flow ---
        composable(Screen.Onboarding.route) { OnboardingScreen(navController = navController) }
        composable(Screen.Login.route) { LoginScreen(navController = navController) }
        composable(Screen.Register.route) { RegisterScreen(navController = navController) }
        composable(Screen.WeightPicker.route) { WeightPickerScreen(navController = navController) }
        composable(Screen.HeightPicker.route) { HeightPickerScreen(navController = navController) }
        composable(Screen.GoalSelection.route) { GoalSelectionScreen(navController = navController, userViewModel = userViewModel) }
        composable(Screen.GenderSelection.route) { GenderSelectionScreen(navController = navController, userViewModel = userViewModel) }
        composable(Screen.PhysicalActivityLevel.route) { PhysicalActivityLevelScreen(navController = navController) }

        // --- Main App Screens ---
        composable(Screen.Profile.route) { ProfileScreen(navController = navController) }
        // Bodybuilder Spilts
        composable(Screen.BodybuilderSplits.route) { BodybuilderSplitsScreen(navController = navController) }
        composable(Screen.ArnoldWorkout.route) { ArnoldWorkoutScreen(navController = navController) }
        composable(Screen.SteveReevesWorkout.route) {
            SteveReevesWorkoutScreen(navController = navController)
        }
        composable(Screen.RonnieWorkout.route) {
            RonnieColemanWorkoutScreen(navController = navController)
        }
        composable(Screen.CbumWorkout.route) {
            ChrisBumsteadWorkoutScreen(navController = navController)
        }
        composable(Screen.JayCutlerWorkout.route) {
            JayCutlerWorkoutScreen(navController = navController)
        }



        // --- Specialty Plan Screens ---
        composable(Screen.StrengthTrainingMale.route) { StrengthTrainingMaleScreen(navController = navController) }
        composable(Screen.StrengthTrainingFemale.route) { StrengthTrainingFemaleScreen(navController = navController) }

        // --- Home Dashboards ---
        composable(Screen.BeginnerHome.route) { BeginnerHomeScreen(navController = navController) }
        composable(Screen.IntermediateHome.route) { IntermediateHomeScreen(navController = navController) }
        composable(Screen.AdvanceHome.route) { AdvanceHomeScreen(navController = navController) }

        // --- Main Section & Workout Grids ---
        composable(Screen.HomeWorkouts.route) { HomeWorkoutsScreen(navController = navController) }
        composable(Screen.BeginnerWorkouts.route) { BeginnerWorkoutsScreen(navController = navController) }
        composable(Screen.IntermediateWorkouts.route) { IntermediateWorkoutsScreen(navController = navController) }
        composable(Screen.AdvanceWorkouts.route) { AdvanceWorkoutsScreen(navController = navController) }

        // --- Home Workout Details ---
        composable(Screen.FullBodyHomeWorkouts.route) { FullBodyHomeWorkoutsScreen(navController = navController) }
        composable(Screen.LowerBodyHomeWorkouts.route) { LowerBodyHomeWorkoutScreen(navController = navController) }
        composable(Screen.UpperBodyHomeWorkouts.route) { UpperBodyHomeWorkoutScreen(navController = navController) }
        composable(Screen.HomeAbsWorkouts.route) { HomeAbsWorkoutScreen(navController = navController) }

        // --- Beginner Workout Details ---
        composable(Screen.BeginnerChestWorkouts.route) { BeginnerChestWorkoutsScreen(navController) }
        composable(Screen.BeginnerShoulderWorkouts.route) { BeginnerShoulderWorkoutsScreen(navController) }
        composable(Screen.BeginnerArmsWorkouts.route) { BeginnerArmsWorkoutsScreen(navController) }
        composable(Screen.BeginnerLegsWorkouts.route) { BeginnerLegsWorkoutsScreen(navController) }
        composable(Screen.BeginnerAbsWorkouts.route) { BeginnerAbsWorkoutsScreen(navController) }
        composable(Screen.BeginnerBackWorkouts.route) { BeginnerBackWorkoutsScreen(navController) }

        // --- Intermediate Workout Details ---
        composable(Screen.IntermediateChestWorkouts.route) { IntermediateChestWorkoutsScreen(navController) }
        composable(Screen.IntermediateShoulderWorkouts.route) { IntermediateShoulderWorkoutsScreen(navController) }
        composable(Screen.IntermediateArmsWorkouts.route) { IntermediateArmsWorkoutsScreen(navController) }
        composable(Screen.IntermediateLegsWorkouts.route) { IntermediateLegsWorkoutsScreen(navController) }
        composable(Screen.IntermediateAbsWorkouts.route) { IntermediateAbsWorkoutsScreen(navController) }
        composable(Screen.IntermediateBackWorkouts.route) { IntermediateBackWorkoutsScreen(navController) }

        // blog screen
        // Inside your NavHost block
        composable("fitness_blog") {
            FitnessBlogScreen(navController = navController)
        }
        composable("overcoming_plateaus") {
            OvercomingWorkoutPlateausScreen(navController = navController)
        }
        // Inside your NavHost block
        composable("muscle_soreness") {
            MuscleSorenessScreen(navController = navController)
        }
        composable("importance_of_rest") {
            ImportanceOfRestScreen(navController = navController)
        }
        // Inside NavHost
        composable("nutrition_muscle_gain") {
            NutritionMuscleGainScreen(navController = navController)
        }
        // Inside NavHost
        composable("preventing_injuries") {
            PreventingInjuriesScreen(navController = navController)
        }



        // --- Advance Workout Details ---
        composable(Screen.AdvanceChestWorkouts.route) { AdvanceChestWorkoutsScreen(navController) }
        composable(Screen.AdvanceShoulderWorkouts.route) { AdvanceShoulderWorkoutsScreen(navController) }
        composable(Screen.AdvanceArmsWorkouts.route) { AdvanceArmsWorkoutsScreen(navController) }
        composable(Screen.AdvanceLegsWorkouts.route) { AdvanceLegsWorkoutsScreen(navController) }
        composable(Screen.AdvanceAbsWorkouts.route) { AdvanceAbsWorkoutsScreen(navController) }
        composable(Screen.AdvanceBackWorkouts.route) { AdvanceBackWorkoutsScreen(navController) }

        // --- Tools Screen ---
        // 👇 THIS IS THE CORRECTED LINE
        composable("tools") { ToolsScreen(navController = navController, userViewModel = userViewModel) }

        composable("diet_overview") {
            DietOverviewScreen(navController = navController)
        }



        composable("video_analysis") {
            AIFormCorrectorScreen(navController = navController)
        }
    }
}
