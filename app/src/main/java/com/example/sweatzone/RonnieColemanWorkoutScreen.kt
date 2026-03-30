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
fun RonnieColemanWorkoutScreen(navController: NavController) {
    val bgColor = Color(0xFFFFFFFF) // White background like the image
    val primaryTextColor = Color(0xFF1C1C1E)
    val secondaryTextColor = Color.Gray
    val purpleAccent = Color(0xFF9C27B0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Bodybuilder Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Favorite */ }) {
                        Icon(Icons.Default.Favorite, "Favorite", tint = Color.Black)
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
            // Header Image Card
            RonnieHeader()

            // Main Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Career History
                SectionWithIcon(Icons.Default.History, "Career History", primaryTextColor) {
                    Text(
                        text = "Ronnie Coleman is widely regarded as the greatest bodybuilder of all time. Before his reign, he served as a police officer in Arlington, Texas. He dominated the sport from 1998 to 2005, winning the Mr. Olympia title eight consecutive times, tying the record held by Lee Haney. Known for his unimaginable size and strength, he redefined the \"mass monster\" era.",
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
                                text = "\"Everybody wants to be a bodybuilder, but nobody wants to lift no heavy-ass weights.\"",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold, // Bold quote
                                color = primaryTextColor,
                                lineHeight = 20.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Coleman's philosophy centered on heavy compound movements and high volume. Unlike many contemporaries who practiced the \"pump,\" Ronnie aimed to move maximum weight for repetitions, believing that size comes directly from strength.",
                                fontSize = 13.sp,
                                color = secondaryTextColor,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }

                // Core Principles
                SectionWithIcon(Icons.Default.FitnessCenter, "Core Principles", purpleAccent) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            PrincipleBox(Icons.Default.TrendingUp, "Progressive Overload", "Always increasing weight or reps.", Modifier.weight(1f))
                            PrincipleBox(Icons.Default.Repeat, "High Frequency", "Hitting each body part twice a week.", Modifier.weight(1f))
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            PrincipleBox(Icons.Default.Straighten, "Full ROM", "Complete stretch and contraction.", Modifier.weight(1f))
                            PrincipleBox(Icons.Default.Restaurant, "Nutrition", "Massive caloric surplus for growth.", Modifier.weight(1f))
                        }
                    }
                }

                // Full Weekly Schedule
                SectionWithIcon(Icons.Default.CalendarToday, "Full Weekly Schedule", primaryTextColor) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        // Monday
                        RonnieScheduleDay(
                            day = "Monday",
                            tags = listOf("BACK", "BICEPS", "SHOULDERS"),
                            tagColor = Color(0xFFE1BEE7) // Light Purple
                        ) {
                            ExerciseRow("Deadlifts", "4 x 6-12")
                            ExerciseRow("Barbell Rows", "3 x 10-12")
                            ExerciseRow("T-Bar Rows", "3 x 10-12")
                            ExerciseRow("One-Arm Dumbbell Row", "3 x 10-12")
                            ExerciseRow("Seated Dumbbell Press", "4 x 12")
                        }

                        // Tuesday
                        RonnieScheduleDay(
                            day = "Tuesday",
                            tags = listOf("LEGS"),
                            tagColor = Color(0xFFE1BEE7)
                        ) {
                            ExerciseRow("Squats", "5-6 x 2-12")
                            ExerciseRow("Leg Press", "4 x 12")
                            ExerciseRow("Walking Lunges", "2 x 100 yds")
                            ExerciseRow("Stiff-Leg Deadlifts", "3 x 12")
                            ExerciseRow("Seated Hamstring Curls", "3 x 12")
                        }

                        // Wednesday
                        RonnieScheduleDay(
                            day = "Wednesday",
                            tags = listOf("CHEST", "TRICEPS"),
                            tagColor = Color(0xFFE1BEE7)
                        ) {
                            ExerciseRow("Bench Press", "5 x 12")
                            ExerciseRow("Incline Barbell Press", "3 x 12")
                            ExerciseRow("Flat Dumbbell Press", "3 x 12")
                            ExerciseRow("Side Lateral Raise", "4 x 12")
                            ExerciseRow("Skullcrushers", "4 x 12")
                        }

                        // Thursday (Similar to Monday)
                        RonnieScheduleDay(
                            day = "Thursday",
                            tags = listOf("BACK", "BICEPS", "SHOULDERS"),
                            tagColor = Color(0xFFE1BEE7)
                        ) {
                            ExerciseRow("Barbell Rows", "5 x 10-12")
                            ExerciseRow("Low Pulley Rows", "4 x 10-12")
                            ExerciseRow("Lat Pulldowns", "3 x 10-12")
                            ExerciseRow("Front Dumbbell Raises", "4 x 12")
                            ExerciseRow("Preacher Curls", "4 x 12")
                        }

                        // Friday (Legs 2)
                        RonnieScheduleDay(
                            day = "Friday",
                            tags = listOf("LEGS"),
                            tagColor = Color(0xFFE1BEE7)
                        ) {
                            ExerciseRow("Front Squats", "4 x 12")
                            ExerciseRow("Hack Squats", "3 x 12")
                            ExerciseRow("Leg Extensions", "3 x 12")
                            ExerciseRow("Standing Leg Curls", "3 x 12")
                        }

                        // Saturday (Chest 2)
                        RonnieScheduleDay(
                            day = "Saturday",
                            tags = listOf("CHEST", "TRICEPS", "CALVES"),
                            tagColor = Color(0xFFE1BEE7)
                        ) {
                            ExerciseRow("Incline Dumbbell Press", "4 x 12")
                            ExerciseRow("Decline Barbell Press", "3 x 12")
                            ExerciseRow("Dumbbell Flyes", "3 x 12")
                            ExerciseRow("Seated French Press", "4 x 12")
                            ExerciseRow("Donkey Calf Raises", "4 x 12")
                        }

                        // Sunday
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F5E9)), // Light Green
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Spa, null, tint = Color(0xFF4CAF50))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Sunday", fontWeight = FontWeight.Bold)
                                        Text("RECOVERY", fontSize = 10.sp, color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                                    }
                                    Text("Rest Day", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                    Text("Focus on nutrition and sleep", fontSize = 12.sp, color = Color.Gray)
                                }
                            }
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
private fun RonnieHeader() {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ronnie), // Ensure you have ronnie.jpg
                contentDescription = "Ronnie Coleman",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black), startY = 300f))
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text("Ronnie Coleman", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                Text("8x Mr. Olympia • The King", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@Composable
private fun SectionWithIcon(icon: ImageVector, title: String, iconColor: Color, content: @Composable () -> Unit) {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, null, tint = iconColor, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
        }
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

@Composable
private fun PrincipleBox(icon: ImageVector, title: String, subtitle: String, modifier: Modifier = Modifier) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFAFAFA)), // Very light gray
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, null, tint = Color(0xFF9C27B0), modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(title, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(subtitle, fontSize = 10.sp, color = Color.Gray, lineHeight = 14.sp)
        }
    }
}

@Composable
private fun RonnieScheduleDay(
    day: String,
    tags: List<String>,
    tagColor: Color,
    exercises: @Composable ColumnScope.() -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(day, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                tags.forEach { tag ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(tagColor.copy(alpha = 0.3f))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(tag, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7B1FA2))
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                exercises()
            }
        }
    }
}

@Composable
private fun ExerciseRow(name: String, sets: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_dumbbell), // Use a default icon or create a vector asset
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
        Text(sets, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
    }
}

// Dummy resource for the icon if you don't have one, replaced by a standard vector below
// You can replace R.drawable.ic_dumbbell with Icons.Default.FitnessCenter in the usage above if needed.

@Preview(showBackground = true)
@Composable
fun RonnieColemanWorkoutScreenPreview() {
    SweatzoneTheme {
        RonnieColemanWorkoutScreen(rememberNavController())
    }
}
