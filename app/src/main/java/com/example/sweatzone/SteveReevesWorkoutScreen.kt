package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.theme.SweatzoneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteveReevesWorkoutScreen(navController: NavController) {
    val bgColor = Color(0xFFF3F4F8)
    val primaryTextColor = Color(0xFF1C1C1E)
    val secondaryTextColor = Color.Gray
    val accentColor = Color(0xFF9C27B0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Legendary Profile", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* More options */ }) {
                        Icon(Icons.Default.MoreVert, "More options")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = { AppBottomNavigationBar(navController = navController, homeRoute = Screen.BeginnerHome.route) },
        containerColor = bgColor
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Section
            SteveReevesHeader()

            // Achievement Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                AchievementPill("Mr. America 1947")
                AchievementPill("Mr. Universe 1950")
                AchievementPill("Calves Pro")
            }

            // Main Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                LegacyAndCareerSection(primaryTextColor, secondaryTextColor)
                WorkoutPhilosophySectionSteve(accentColor, primaryTextColor)
                TrainingPrinciplesSection(primaryTextColor, secondaryTextColor, accentColor)
                WeeklyTrainingScheduleSection(primaryTextColor, accentColor)

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- UI SECTIONS specific to Steve Reeves ---

@Composable
private fun SteveReevesHeader() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)) {
        Image(
            painter = painterResource(id = R.drawable.steve_reeves), // Add steve_reeves.jpg
            contentDescription = "Steve Reeves",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(0.7f)), startY = 300f))
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text("Steve Reeves", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("The Classic Ideal", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        }
    }
}

@Composable
private fun AchievementPill(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(20.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun LegacyAndCareerSection(primaryColor: Color, secondaryColor: Color) {
    Column {
        Text("Legacy & Career", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Steve Reeves is widely considered the most aesthetic bodybuilder of all time. Before becoming an international movie star as Hercules, Reeves dominated the bodybuilding world with his \"X\" physique—impossibly broad shoulders, a wide back, and a tiny waist. He championed physique perfection through symmetry rather than sheer mass.",
            fontSize = 15.sp,
            color = secondaryColor,
            lineHeight = 22.sp
        )
    }
}

@Composable
private fun WorkoutPhilosophySectionSteve(accentColor: Color, textColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "\"I don't believe in bodybuilding using steroids. I believe in using nature in got more. It takes longer, but the result is better.\"",
                fontSize = 14.sp,
                color = textColor.copy(alpha = 0.7f),
                lineHeight = 20.sp
            )
            Text(
                text = "— STEVE REEVES",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = accentColor,
                modifier = Modifier.align(Alignment.End).padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun TrainingPrinciplesSection(primaryColor: Color, secondaryColor: Color, accentColor: Color) {
    Column {
        Text("Training Principles", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            PrincipleItem(Icons.Default.CalendarToday, "Full Body Frequency", "Train the entire body 3 times per week, allowing for complete recovery days in between.", accentColor)
            PrincipleItem(Icons.Default.Timer, "Controlled Tempo", "3 seconds up, 3 seconds down. Eliminate momentum to ensure maximum muscle engagement.", accentColor)
            PrincipleItem(Icons.Default.Psychology, "Mind-Muscle Connection", "Utmost concentration on the muscle working during every single repetition.", accentColor)
        }
    }
}

@Composable
private fun WeeklyTrainingScheduleSection(primaryColor: Color, accentColor: Color) {
    Column {
        Text("Weekly Training Schedule", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        Spacer(modifier = Modifier.height(16.dp))
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            // Monday
            ScheduleDayWithExercises(
                day = "Monday",
                title = "Full Body Foundation",
                accentColor = accentColor,
                exercises = {
                    ScheduleExerciseItem(title = "Upright Row", muscleGroup = "Shoulders & Traps", sets = "3 Sets", reps = "8-12 Reps")
                    ScheduleExerciseItem(title = "Bench Press", muscleGroup = "Chest", sets = "2 Sets", reps = "8-12 Reps")
                    ScheduleExerciseItem(title = "Squats", muscleGroup = "Legs/Quads", sets = "3 Sets", reps = "8-12 Reps")
                }
            )
            // Tuesday
            ScheduleDayRest("Tuesday", "Rest & Recovery", "Active rest day. Light walking or swimming to promote circulation without stressing the muscles.")
            // Wednesday
            ScheduleDayWithExercises(
                day = "Wednesday",
                title = "Full Body Power",
                accentColor = accentColor,
                exercises = {
                    ScheduleExerciseItem(title = "Dumbbell Row", muscleGroup = "Back", sets = "3 Sets", reps = "8-12 Reps")
                    ScheduleExerciseItem(title = "Military Press", muscleGroup = "Shoulders", sets = "3 Sets", reps = "8-12 Reps")
                    ScheduleExerciseItem(title = "Barbell Curls", muscleGroup = "Biceps", sets = "3 Sets", reps = "8-12 Reps")
                }
            )
            // Thursday
            ScheduleDayRest("Thursday", "Rest & Recovery", "Focus on nutrition. Ensure high protein intake to support muscle repair from the previous sessions.")
            // Friday
            ScheduleDayWithExercises(
                day = "Friday",
                title = "Full Body Volume",
                accentColor = accentColor,
                exercises = {
                    ScheduleExerciseItem(title = "Incline Press", muscleGroup = "Upper Chest", sets = "3 Sets", reps = "8-12 Reps")
                    ScheduleExerciseItem(title = "Triceps Pushdown", muscleGroup = "Triceps", sets = "3 Sets", reps = "8-12 Reps")
                    ScheduleExerciseItem(title = "Calf Raises", muscleGroup = "Calves", sets = "3 Sets", reps = "15-20 Reps")
                }
            )
            // Weekend
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ScheduleDayRest("Saturday", "Total Rest", "No physical activity.", Modifier.weight(1f))
                ScheduleDayRest("Sunday", "Total Rest", "Prepare for next week.", Modifier.weight(1f))
            }
        }
    }
}


// --- HELPER COMPOSABLES ---

@Composable
private fun PrincipleItem(icon: ImageVector, title: String, description: String, accentColor: Color) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = accentColor, modifier = Modifier.padding(top = 2.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(description, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
private fun ScheduleDayWithExercises(day: String, title: String, accentColor: Color, exercises: @Composable ColumnScope.() -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(accentColor),
                contentAlignment = Alignment.Center
            ) {
                Text(day.first().toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
            Box(modifier = Modifier.width(1.dp).height(16.dp).background(accentColor.copy(alpha = 0.3f)))
        }
        Column(Modifier.padding(start = 8.dp)) {
            Text(day, fontWeight = FontWeight.Bold)
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Spacer(Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                exercises()
            }
        }
    }
}

@Composable
private fun ScheduleDayRest(day: String, title: String, description: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(48.dp)) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).border(1.dp, Color.LightGray, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(day.first().toString(), color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }
        Column(Modifier.padding(start = 8.dp)) {
            Text(title, fontWeight = FontWeight.Bold, color = Color.Gray)
            Text(description, fontSize = 12.sp, color = Color.Gray, lineHeight = 18.sp)
        }
    }
}

@Composable
fun ScheduleExerciseItem(title: String, muscleGroup: String, sets: String, reps: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.FitnessCenter, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(muscleGroup, fontSize = 12.sp, color = Color.Gray)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(sets, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                Text(reps, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}


// --- Preview ---

@Preview(showBackground = true)
@Composable
fun SteveReevesWorkoutScreenPreview() {
    SweatzoneTheme {
        SteveReevesWorkoutScreen(rememberNavController())
    }
}
