package com.example.sweatzone

import androidx.compose.foundation.BorderStroke
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
fun ChrisBumsteadWorkoutScreen(navController: NavController) {
    val bgColor = Color(0xFFF9F9F9) // Off-white background
    val primaryTextColor = Color(0xFF1C1C1E)
    val secondaryTextColor = Color.Gray
    val purpleAccent = Color(0xFF9C27B0)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Empty Title */ },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
                    }
                },
                actions = {
                    IconButton(
                        onClick = { /* Favorite */ },
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    ) {
                        Icon(Icons.Default.FavoriteBorder, "Favorite", tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
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
            CbumHeader()

            // Main Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // The Legend
                SectionWithHeader("The Legend", primaryTextColor) {
                    Text(
                        text = "Reigning king of the Classic Physique division, Chris \"CBum\" Bumstead has single-handedly revived the aesthetics of the Golden Era. Known for his vacuum pose, sweeping lats, and focus on proportion over mass monster size.",
                        fontSize = 14.sp,
                        color = secondaryTextColor,
                        lineHeight = 20.sp
                    )
                }

                // Philosophy
                SectionWithHeader("Philosophy", primaryTextColor) {
                    Text(
                        text = "\"I don't train to lift heavy weights; I train to build muscle. The weight is just a tool.\"",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryTextColor,
                        lineHeight = 22.sp
                    )
                }

                // Core Principles
                SectionWithHeader("Core Principles", primaryTextColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            PrincipleBox(Icons.Default.Psychology, "Mind-Muscle", Modifier.weight(1f))
                            PrincipleBox(Icons.Default.Timer, "Time Under Tension", Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            PrincipleBox(Icons.Default.FitnessCenter, "Compound Lifts", Modifier.weight(1f))
                            PrincipleBox(Icons.Default.TrendingUp, "Progressive Load", Modifier.weight(1f))
                        }
                    }
                }

                // Weekly Schedule
                SectionWithHeader("Weekly Schedule", primaryTextColor, "7 Day Split") {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        CbumScheduleDay("Monday", "BACK WIDTH", "Deadlifts, Rack Pulls, Wide Grip Lat Pulldowns, Single Arm Rows.")
                        CbumScheduleDay("Tuesday", "CHEST & BICEPS", "Incline DB Press, Pec Deck Flyes, Seated DB curls, Hammer Curls.")
                        CbumScheduleDay("Wednesday", "HAMS & GLUTES", "Romanian Deadlifts, Lying Leg Curls, Glute Kickbacks, Lunges.")
                        CbumScheduleDay("Thursday", "SHOULDERS & TRICEPS", "Seated DB Press, Lateral Raises, Reverse Pec Deck, Rope Pushdowns.")
                        CbumScheduleDay("Friday", "QUADS", "Barbell Squats, Leg Press, Leg Extensions, Sissy Squats.")
                        CbumScheduleDay("Saturday", "Active Recovery / Cardio")
                        CbumScheduleDay("Sunday", "Rest Day")
                    }
                }

                // Sample Back Workout
                SectionWithHeader("Sample Back Workout", primaryTextColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SampleExerciseCard("Deadlifts", "Main Compound", "3", "8-10")
                        SampleExerciseCard("Bent Over Barbell Rows", "Underhand Grip", "3", "10-12")
                        SampleExerciseCard("Wide Grip Lat Pulldowns", "Slow Negatives", "3", "12-15")
                        SampleExerciseCard("Seated Cable Rows", "V-Bar Attachment", "4", "12-15")
                    }
                }



                Spacer(Modifier.height(24.dp))
            }
        }
    }
}

// --- UI SECTIONS & HELPERS ---

@Composable
private fun CbumHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.cbum), // Ensure you have cbum.jpg
            contentDescription = "Chris Bumstead",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color(0xFFF9F9F9)),
                        startY = 500f
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Text("Chris Bumstead", color = Color.Black, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("5X CLASSIC PHYSIQUE OLYMPIA", color = Color(0xFF9C27B0), fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SectionWithHeader(
    title: String,
    titleColor: Color,
    tag: String? = null,
    content: @Composable () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = titleColor)
            if (tag != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray.copy(alpha = 0.3f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(tag, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        content()
    }
}

@Composable
private fun PrincipleBox(icon: ImageVector, title: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(icon, null, tint = Color(0xFF9C27B0))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun CbumScheduleDay(day: String, tag: String, exercises: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(day, modifier = Modifier.width(80.dp), color = Color.Gray, fontSize = 14.sp)
        Column {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF9C27B0).copy(alpha = 0.1f))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(tag, color = Color(0xFF9C27B0), fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            if (exercises != null) {
                Spacer(Modifier.height(4.dp))
                Text(exercises, color = Color.Gray, fontSize = 12.sp, lineHeight = 18.sp)
            }
        }
    }
    Divider(modifier = Modifier.padding(top = 12.dp), color = Color.LightGray.copy(alpha = 0.5f))
}

@Composable
fun SampleExerciseCard(title: String, subtitle: String, sets: String, reps: String) {
    val isHeader = title == "Deadlifts" // Special styling for the first item
    val bgColor = if (isHeader) Color(0xFF1C1C1E) else Color.White
    val textColor = if (isHeader) Color.White else Color.Black
    val subTextColor = if (isHeader) Color.White.copy(alpha = 0.7f) else Color.Gray

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, color = textColor)
                Text(subtitle, fontSize = 12.sp, color = subTextColor)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(sets, fontWeight = FontWeight.Bold, color = textColor, fontSize = 18.sp)
                Text("Sets", fontSize = 10.sp, color = subTextColor)
            }
            Spacer(Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.End) {
                Text(reps, fontWeight = FontWeight.Bold, color = textColor, fontSize = 18.sp)
                Text("Reps", fontSize = 10.sp, color = subTextColor)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun ChrisBumsteadWorkoutScreenPreview() {
    SweatzoneTheme {
        ChrisBumsteadWorkoutScreen(navController = rememberNavController())
    }
}
