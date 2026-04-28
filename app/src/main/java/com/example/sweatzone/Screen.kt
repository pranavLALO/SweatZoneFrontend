package com.example.sweatzone

sealed class Screen(val route: String) {
    // --- Setup and Onboarding ---
    object Onboarding : Screen("onboarding")
    object Login : Screen("login")
    object Register : Screen("register")
    object GenderSelection : Screen("gender_selection")
    object GoalSelection : Screen("goal_selection")
    object WeightPicker : Screen("weight_picker")
    object HeightPicker : Screen("height_picker")
    object PhysicalActivityLevel : Screen("physical_activity_level")

    // --- Main App Screens ---
    object Profile : Screen("profile")


    // --- Specific Plans ---
    object StrengthTrainingMale : Screen("strength_training_male")
    object StrengthTrainingFemale : Screen("strength_training_female")

    // --- Home Dashboards ---
    object BeginnerHome : Screen("beginner_home")
    object IntermediateHome : Screen("intermediate_home")
    object AdvanceHome : Screen("advance_home")

    // --- Main Section Screens ---
    object HomeWorkouts : Screen("home_workouts")
    object FullBodyHomeWorkouts : Screen("full_body_home_workouts")
    object LowerBodyHomeWorkouts : Screen("lower_body_home_workouts")
    object HomeAbsWorkouts : Screen("home_abs_workouts")
    object UpperBodyHomeWorkouts : Screen("upper_body_home_workouts")

    // --- Workout Level Grids ---
    object BeginnerWorkouts : Screen("beginner_workouts")
    object IntermediateWorkouts : Screen("intermediate_workouts")
    object AdvanceWorkouts : Screen("advance_workouts") // Added missing route
    object Warmup : Screen("warmup")

    // --- Workout Detail Screens ---
    object BeginnerChestWorkouts : Screen("beginner_chest_workouts")
    object BeginnerShoulderWorkouts : Screen("beginner_shoulder_workouts")
    object BeginnerArmsWorkouts : Screen("beginner_arms_workouts")
    object BeginnerLegsWorkouts : Screen("beginner_legs_workouts")
    object BeginnerAbsWorkouts : Screen("beginner_abs_workouts")
    object BeginnerBackWorkouts : Screen("beginner_back_workouts")

    object IntermediateChestWorkouts : Screen("intermediate_chest_workout")
    object IntermediateShoulderWorkouts : Screen("intermediate_shoulder_workout")
    object IntermediateArmsWorkouts : Screen("intermediate_arms_workout")
    object IntermediateLegsWorkouts : Screen("intermediate_legs_workout")
    object IntermediateAbsWorkouts : Screen("intermediate_abs_workout")
    object IntermediateBackWorkouts : Screen("intermediate_back_workout")

    object AdvanceChestWorkouts : Screen("advance_chest_workout")
    object AdvanceShoulderWorkouts : Screen("advance_shoulder_workout")
    object AdvanceArmsWorkouts : Screen("advance_arms_workout")
    object AdvanceLegsWorkouts : Screen("advance_legs_workout")
    object AdvanceAbsWorkouts : Screen("advance_abs_workout")
    object AdvanceBackWorkouts : Screen("advance_back_workout")
//Bodybuilder Spilts

    object BodybuilderSplits : Screen("bodybuilder_splits")
    object ArnoldWorkout : Screen("arnold_workout")
    object RonnieWorkout : Screen("ronnie_workout")
    object CbumWorkout : Screen("cbum_workout")
    object JayCutlerWorkout : Screen("jay_cutler_workout")




    object SteveReevesWorkout : Screen("steve_reeves_workout")
    object VideoPlayer : Screen("video_player/{videoResId}")
    object WorkoutSummary : Screen("workout_summary/{muscleGroup}")
    object PersonalizedProgress : Screen("personalized_progress")
}