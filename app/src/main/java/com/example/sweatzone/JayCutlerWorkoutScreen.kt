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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.theme.SweatzoneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JayCutlerWorkoutScreen(navController: NavController) {
    val bgColor = Color(0xFFFFFFFF)
    val primaryTextColor = Color(0xFF1C1C1E)
    val secondaryTextColor = Color.Gray
    val purpleAccent = Color(0xFF9C27B0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Athlete Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Share */ }) {
                        Icon(Icons.Default.IosShare, "Share")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = bgColor)
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
            JayCutlerHeader()

            // Main Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Career History
                SectionWithIcon(Icons.Default.History, "Career History", purpleAccent) {
                    Text(
                        text = "A dominant force in the 2000s, Jay Cutler is best known for ending Ronnie Coleman's eight-year reign. He secured the Sandow statue four times (2006, 2007, 2009, 2011) and remains the only bodybuilder in history to reclaim the Mr. Olympia title after losing it.",
                        fontSize = 14.sp,
                        color = secondaryTextColor,
                        lineHeight = 20.sp
                    )
                }

                // Workout Philosophy
                SectionWithIcon(Icons.Default.Psychology, "Workout Philosophy", purpleAccent) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3F0F7)), // Light purple bg
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "\"I train for the pump, not the ego. It's not about how much you lift, it's about how much you feel after you lift.\"",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryTextColor,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Cutler advocated for \"high-volume training with moderate weights. He focuses on filling the muscle with blood (\"the pump\"), using short rest periods of 45-60 seconds, prioritizing muscle contraction and heavy lifting.",
                                fontSize = 13.sp,
                                color = secondaryTextColor,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Core Principles
                SectionWithIcon(Icons.Default.FitnessCenter, "Core Principles", primaryTextColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        PrincipleRow(Icons.Default.BubbleChart, "Volume over Load", "Focus on 12-15+ reps to maximize hypertrophy.")
                        PrincipleRow(Icons.Default.Person, "Unilateral Training", "Correct imbalances by training each limb/side individually.")
                        PrincipleRow(Icons.Default.HourglassTop, "Active Breathing", "Utilizing forced breaths between sets for short recovery.")
                    }
                }

                // Full Weekly Schedule
                SectionWithIcon(Icons.Default.CalendarToday, "Full Weekly Schedule", primaryTextColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        JayScheduleDay("Chest & Calves", "MORNING") {
                            ExerciseRow("Incline Barbell Press", "3 sets • 10-12 reps")
                            ExerciseRow("Flat Dumbbell Press", "3 sets • 10-12 reps")
                            ExerciseRow("Incline DB Flys", "3 sets • 12 reps")
                            ExerciseRow("Cable Crossovers", "3 sets • 12-15 reps")
                            ExerciseRow("Standing Calf Raises", "4 sets • 12-15 reps")
                            ExerciseRow("Seated Calf Raises", "3 sets • 15-20 reps")
                        }
                        JayScheduleDay("Arms (Biceps & Triceps)", "EVENING") {
                            ExerciseRow("Barbell Curls", "5 sets • 15 reps")
                            ExerciseRow("Preacher Curls", "3 sets • 12-15 reps")
                            ExerciseRow("DB Hammer Curls", "2 sets • 12-15 reps")
                            ExerciseRow("Tricep Pushdowns", "4 sets • 12-15 reps")
                            ExerciseRow("Close-Grip Bench", "3 sets • 10-12 reps")
                        }
                        JayScheduleDay("Rest & Recovery", "FULL DAY") {
                            Text("Active recovery, stretching, or light cardio.", color = secondaryTextColor, fontSize = 14.sp)
                        }
                        JayScheduleDay("Back Focus", "MORNING") {
                            ExerciseRow("Lat Pulldowns", "3 sets • 10-12 reps")
                            ExerciseRow("Bent-Over Rows", "4 sets • 10 reps")
                            ExerciseRow("One-Arm Rows", "3 sets • 10 reps")
                            ExerciseRow("Deadlifts", "3 sets • 8-10 reps")
                            ExerciseRow("Hyperextensions", "3 sets • 15 reps")
                        }
                        JayScheduleDay("Shoulders", "EVENING") {
                            ExerciseRow("Overhead DB Press", "3 sets • 10-12 reps")
                            ExerciseRow("Lateral Raises", "5 sets • 12 reps")
                            ExerciseRow("Front DB Raises", "2 sets • 10 reps")
                            ExerciseRow("Rear Delt Flyes", "3 sets • 12 reps")
                            ExerciseRow("Barbell Shrugs", "4 sets • 12 reps")
                        }
                        JayScheduleDay("Legs (Quad Focus)", "FULL DAY") {
                            ExerciseRow("Leg Extensions", "4 sets • 15-20 reps")
                            ExerciseRow("Barbell Squats", "5 sets • 10-12 reps")
                            ExerciseRow("Leg Press", "4 sets • 12-15 reps")
                            ExerciseRow("Walking Lunges", "3 sets • 20 yards")
                            ExerciseRow("Lying Leg Curls", "4 sets • 12 reps")
                            ExerciseRow("Stiff-Leg Deadlifts", "3 sets • 12 reps")
                        }
                        JayScheduleDay("Recovery Day", "FULL DAY") {
                            Text("Complete rest or very light cardio like walking.", color = secondaryTextColor, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- HELPER COMPOSABLES ---

@Composable
private fun JayCutlerHeader() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Card(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 16.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.jay_cutler), // Ensure you have jay_cutler.jpg
                    contentDescription = "Jay Cutler",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Tag
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF9C27B0))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text("4X MR. O", color = Color.White, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Jay Cutler", color = Color.Black, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
        Text("4x Mr. Olympia • The Quad Stomp", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            StatItem(Icons.Default.FitnessCenter, "6' 1\"", "Height")
            StatItem(Icons.Default.MonitorWeight, "260 LBS", "Weight")
        }
    }
}

@Composable
private fun StatItem(icon: ImageVector, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, contentDescription = null, tint = Color(0xFF9C27B0), modifier = Modifier.size(16.dp))
        Spacer(Modifier.width(8.dp))
        Text(value, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun SectionWithIcon(icon: ImageVector, title: String, iconColor: Color, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
private fun PrincipleRow(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFF3F0F7)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = Color(0xFF9C27B0))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(subtitle, color = Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}

@Composable
private fun JayScheduleDay(title: String, tag: String, exercises: @Composable ColumnScope.() -> Unit) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF1C1C1E)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = tag,
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp
            )
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(Modifier.height(8.dp))
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                exercises()
            }
        }
    }
}

@Composable
private fun ExerciseRow(name: String, setsAndReps: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Text(setsAndReps, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun JayCutlerWorkoutScreenPreview() {
    SweatzoneTheme {
        JayCutlerWorkoutScreen(rememberNavController())
    }
}

