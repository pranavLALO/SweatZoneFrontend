package com.example.sweatzone

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.data.dto.WorkoutHistoryItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizedProgressScreen(navController: NavController, userViewModel: UserViewModel) {
    val history by userViewModel.workoutHistory.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    
    val deepPurple = Color(0xFF673AB7)
    val lightPurple = Color(0xFFD1C4E9)
    val accentColor = Color(0xFFE0FF63)
    val bgGradient = Brush.verticalGradient(listOf(Color(0xFFF3E5F5), Color.White))

    LaunchedEffect(Unit) {
        userViewModel.fetchWorkoutHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Progress Report", fontWeight = FontWeight.Bold, color = deepPurple) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = deepPurple)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().background(bgGradient).padding(padding)) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = deepPurple)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Summary Section
                    item {
                        PerformanceOverview(history ?: emptyList())
                    }

                    item {
                        Text(
                            text = "Recent Achievements",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = deepPurple,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    // History Items
                    val displayHistory = history ?: emptyList()
                    if (displayHistory.isEmpty()) {
                        item {
                            EmptyState()
                        }
                    } else {
                        items(displayHistory) { item ->
                            ProgressSessionCard(item)
                        }
                    }
                    
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}

@Composable
fun PerformanceOverview(history: List<WorkoutHistoryItem>) {
    val totalSessions = history.size
    val totalReps = history.sumOf { it.completed_reps }
    val avgDuration = if (totalSessions > 0) history.sumOf { it.duration_seconds } / totalSessions else 0
    
    val deepPurple = Color(0xFF673AB7)
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Workouts",
            value = totalSessions.toString(),
            icon = Icons.Default.FitnessCenter,
            color = deepPurple
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Total Reps",
            value = totalReps.toString(),
            icon = Icons.Default.Repeat,
            color = Color(0xFF9C27B0)
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "Time (min)",
            value = (avgDuration / 60).toString(),
            icon = Icons.Default.Timer,
            color = Color(0xFFE91E63)
        )
    }
}

@Composable
fun StatCard(modifier: Modifier, title: String, value: String, icon: ImageVector, color: Color) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Text(title, fontSize = 10.sp, color = Color.White.copy(alpha = 0.8f), fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun ProgressSessionCard(item: WorkoutHistoryItem) {
    val deepPurple = Color(0xFF673AB7)
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(deepPurple.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when(item.intensity.lowercase()) {
                        "high", "advanced" -> Icons.Default.Whatshot
                        "medium", "intermediate" -> Icons.Default.Bolt
                        else -> Icons.Default.ElectricBolt
                    },
                    contentDescription = null,
                    tint = deepPurple
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.muscle_group.uppercase(),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = item.completed_at.split(" ")[0], // Date only
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${item.completed_reps} Reps",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = deepPurple
                )
                Text(
                    text = "${(item.duration_seconds / 60)} min",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray.copy(alpha = 0.3f))
        Spacer(modifier = Modifier.height(16.dp))
        Text("No workout sessions yet", color = Color.Gray, fontWeight = FontWeight.Bold)
        Text("Complete a routine to see your progress!", color = Color.Gray, fontSize = 12.sp)
    }
}
