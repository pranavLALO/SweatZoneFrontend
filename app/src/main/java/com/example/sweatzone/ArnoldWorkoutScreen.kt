package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
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
fun ArnoldWorkoutScreen(navController: NavController) {
    val bgColor = Color(0xFFF3F4F8) // Light gray background from the image
    val primaryTextColor = Color(0xFF1C1C1E)
    val secondaryTextColor = Color.Gray
    val accentColor = Color(0xFF9C27B0) // Purple accent from the image

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Athlete Profile", fontWeight = FontWeight.Bold) },
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
            // Header
            HeaderSection()

            // Main Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-50).dp) // Overlap effect
            ) {
                // Career History
                CareerHistorySection(primaryTextColor, secondaryTextColor)
                Spacer(modifier = Modifier.height(24.dp))
                // Workout Philosophy
                WorkoutPhilosophySection(accentColor, primaryTextColor)
                Spacer(modifier = Modifier.height(24.dp))
                // Core Principles
                CorePrinciplesSection(primaryTextColor, secondaryTextColor, accentColor)
                Spacer(modifier = Modifier.height(24.dp))
                // Weekly Schedule
                WeeklyScheduleSection(primaryTextColor, secondaryTextColor, accentColor)
                Spacer(modifier = Modifier.height(24.dp))
                // Sample Routine
                SampleRoutineSection(primaryTextColor, secondaryTextColor, accentColor)
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


// --- UI SECTIONS ---

@Composable
private fun HeaderSection() {
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(250.dp)) {
        Image(
            painter = painterResource(id = R.drawable.arnold),
            contentDescription = "Arnold Schwarzenegger",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient for text visibility
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
            Text("Arnold Schwarzenegger", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text("7x Mr. Olympia • Golden Era Icon", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
        }
    }
}

@Composable
private fun CareerHistorySection(primaryColor: Color, secondaryColor: Color) {
    Text("Career History", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
    Spacer(modifier = Modifier.height(8.dp))
    Text(
        text = "Born in Austria, Arnold dominated bodybuilding in the 1970s, setting the standard for mass and aesthetics. He won the Mr. Olympia title seven times and transitioned into a global Hollywood superstar and politician, earning the nickname \"The Austrian Oak.\"",
        fontSize = 15.sp,
        color = secondaryColor,
        lineHeight = 22.sp
    )
}

@Composable
private fun WorkoutPhilosophySection(accentColor: Color, textColor: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = accentColor.copy(alpha = 0.1f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            Icon(Icons.Default.FormatQuote, null, tint = accentColor, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Workout Philosophy", fontWeight = FontWeight.Bold, color = textColor)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "\"The mind is the limit. As long as the mind can envision the fact that you can do something, you can do it, as long as you really believe 100 percent.\"",
                    fontSize = 14.sp,
                    color = textColor.copy(alpha = 0.7f),
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun CorePrinciplesSection(primaryColor: Color, secondaryColor: Color, accentColor: Color) {
    Text("Core Principles", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        PrinciplePill(
            icon = Icons.Default.Layers,
            title = "High Volume",
            subtitle = "Many sets & reps",
            accentColor = accentColor,
            modifier = Modifier.weight(1f)
        )
        PrinciplePill(
            icon = Icons.Default.Sync,
            title = "Supersets",
            subtitle = "Antagonistic muscles",
            accentColor = accentColor,
            modifier = Modifier.weight(1f)
        )
    }
    Spacer(modifier = Modifier.height(16.dp))
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        PrinciplePill(
            icon = Icons.Default.Bolt,
            title = "Max Intensity",
            subtitle = "Train to failure",
            accentColor = accentColor,
            modifier = Modifier.weight(1f)
        )
        PrinciplePill(
            icon = Icons.Default.Shuffle,
            title = "Muscle Shock",
            subtitle = "Varying routines",
            accentColor = accentColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun WeeklyScheduleSection(primaryColor: Color, secondaryColor: Color, accentColor: Color) {
    Text("Weekly Schedule", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
    Spacer(modifier = Modifier.height(8.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ScheduleDayItem("Monday", "Chest & Back", "Bench press, incline press, pullovers, chin-ups, bent-over rows, deadlifts, crunches", accentColor, secondaryColor)
        ScheduleDayItem("Tuesday", "Shoulders & Arms", "Clean & press, lateral raises, upright rows, barbell curls, seated dumbbell curls, close-grip press", accentColor, secondaryColor)
        ScheduleDayItem("Wednesday", "Legs & Lower Back", "Squats, lunges, leg curls, stiff-leg deadlifts, good mornings, calf raises, leg extensions", accentColor, secondaryColor)
        ScheduleDayItem("Thursday", "Chest & Back", "Bench press, incline press, pullovers, chin-ups, bent-over rows, deadlifts, crunches", accentColor, secondaryColor)
        ScheduleDayItem("Friday", "Shoulders & Arms", "Clean & press, lateral raises, upright rows, barbell curls, seated dumbbell curls, close-grip press", accentColor, secondaryColor)
        ScheduleDayItem("Saturday", "Legs & Lower Back", "Squats, lunges, leg curls, stiff-leg deadlifts, good mornings, calf raises, leg extensions", accentColor, secondaryColor)
        ScheduleDayItem("Sunday", "Rest Day", "Complete rest, active recovery, or light stretching to aid recovery.", Color.Gray, secondaryColor)
    }
}

@Composable
private fun SampleRoutineSection(primaryColor: Color, secondaryColor: Color, accentColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Sample Routine", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = primaryColor)
        TextChip("Chest & Back", accentColor)
    }
    Spacer(modifier = Modifier.height(16.dp))
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        RoutineExerciseItem(Icons.Default.FitnessCenter, "Bench Press", "5 sets • 6-10 reps")
        RoutineExerciseItem(Icons.Default.SportsGymnastics, "Wide-Grip Chins", "5 sets • To failure")
        RoutineExerciseItem(Icons.Default.FitnessCenter, "Incline Barbell Press", "5 sets • 6-10 reps")
        RoutineExerciseItem(Icons.Default.SportsGymnastics, "T-Bar Rows", "5 sets • 10-15 reps")
    }
}


// --- HELPER COMPOSABLES ---

@Composable
private fun PrinciplePill(icon: ImageVector, title: String, subtitle: String, accentColor: Color, modifier: Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, tint = accentColor)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(title, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            Text(subtitle, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
private fun ScheduleDayItem(day: String, split: String, exercises: String, accentColor: Color, secondaryColor: Color) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(day, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                TextChip(split, accentColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(exercises, fontSize = 14.sp, color = secondaryColor, lineHeight = 20.sp)
        }
    }
}

@Composable
fun TextChip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(color.copy(alpha = 0.1f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) // <-- THE MISSING CLOSING PARENTHESIS WAS HERE
    {
        Text(text, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun RoutineExerciseItem(icon: ImageVector, title: String, details: String) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8EAF6)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, null, tint = Color(0xFF3F51B5), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(details, color = Color.Gray, fontSize = 14.sp)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = Color.Gray)
        }
    }
}

// --- Preview ---

@Preview(showBackground = true)
@Composable
fun ArnoldWorkoutScreenPreview() {
    SweatzoneTheme {
        ArnoldWorkoutScreen(rememberNavController())
    }
}

