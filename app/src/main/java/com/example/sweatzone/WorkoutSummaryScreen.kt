package com.example.sweatzone

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.data.dto.LastWorkoutData

@Composable
fun WorkoutSummaryScreen(navController: NavController, muscleGroup: String, userViewModel: UserViewModel) {
    val currentResult by userViewModel.currentWorkoutResult.collectAsState()
    val lastWorkoutState by userViewModel.lastWorkout.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchLastWorkout(muscleGroup)
    }

    val premiumDark = Color(0xFF1A1A1A)
    val premiumAccent = Color(0xFFE0FF63)
    val cardBg = Color(0xFF2C2C2C)

    Scaffold(
        containerColor = premiumDark
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Performance Report",
                color = premiumAccent,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = muscleGroup.replaceFirstChar { it.uppercase() },
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            currentResult?.let { result ->
                val lastData = lastWorkoutState?.data

                // Main Stats Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (result.intensity.lowercase() == "intermediate" || result.intensity.lowercase() == "medium") {
                        // Calculate progress for intermediate
                        val totalPlanned = result.exerciseLogs.size * 3 * 12 // Default fallback if needed
                        // Actually, we should probably pass the progress or calculate it here. 
                        // But we already calculate it in the log session.
                        // For display, let's just show progress based on reps vs a reasonable target if volume is 0.
                        SummaryStatCard(
                            modifier = Modifier.weight(1f),
                            label = "GOAL COMPLETION",
                            value = "${(result.totalReps.toFloat() / (result.totalSets * 12).toFloat() * 100).toInt().coerceIn(0, 100)}%",
                            icon = Icons.Default.CheckCircle,
                            accentColor = premiumAccent
                        )
                    } else {
                        SummaryStatCard(
                            modifier = Modifier.weight(1f),
                            label = "VOLUME",
                            value = "${result.totalVolume} kg",
                            icon = Icons.Default.FitnessCenter,
                            comparison = calculateComparison(result.totalVolume, lastData?.weight_kg ?: 0),
                            accentColor = premiumAccent
                        )
                    }
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "DURATION",
                        value = formatDuration(result.totalTimeSeconds),
                        icon = Icons.Default.Timer,
                        comparison = calculateComparison(result.totalTimeSeconds, lastData?.duration_seconds ?: 0, inverse = true),
                        accentColor = Color.Cyan
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "SETS",
                        value = "${result.totalSets}",
                        icon = Icons.Default.Layers,
                        accentColor = Color.Magenta
                    )
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "REPS",
                        value = "${result.totalReps}",
                        icon = Icons.Default.Repeat,
                        accentColor = Color.Yellow
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Exercise Breakdown
                Text(
                    text = "Exercise Detail",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                result.exerciseLogs.forEach { log ->
                    ExerciseLogItem(log, cardBg)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(40.dp))

                // Action Buttons
                Button(
                    onClick = {
                        userViewModel.logWorkout(
                            muscleGroup = result.muscleGroup,
                            intensity = result.intensity,
                            durationSeconds = result.totalTimeSeconds,
                            weightKg = result.totalVolume / result.totalSets, // Avg weight
                            completedSets = result.totalSets,
                            completedReps = result.totalReps,
                            timerUsed = result.totalTimeSeconds,
                            exerciseLogs = result.exerciseLogs,
                            onSuccess = {
                                userViewModel.clearCurrentWorkoutResult()
                                navController.popBackStack(Screen.AdvanceHome.route, false)
                            }
                        )
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = premiumAccent),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("FINISH WORKOUT", color = Color.Black, fontWeight = FontWeight.ExtraBold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = {
                        userViewModel.clearCurrentWorkoutResult()
                        navController.popBackStack() // Go back to start workout again
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("RETRY SESSION", color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SummaryStatCard(
    modifier: Modifier,
    label: String,
    value: String,
    icon: ImageVector,
    comparison: String? = null,
    accentColor: Color
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = accentColor.copy(alpha = 0.6f), modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            Text(text = value, color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
            
            comparison?.let {
                val isPositive = it.startsWith("+")
                Text(
                    text = it,
                    color = if (isPositive) Color.Green else Color.Red,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun ExerciseLogItem(log: com.example.sweatzone.data.dto.ExerciseLog, bg: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = log.exercise_title, color = Color.White, fontWeight = FontWeight.Bold)
            Text(text = "${log.sets_completed} Sets • ${log.reps_completed} Reps", color = Color.Gray, fontSize = 12.sp)
        }
        Text(text = "${log.weight_kg} kg", color = Color(0xFFE0FF63), fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }
}

fun calculateComparison(current: Int, last: Int, inverse: Boolean = false): String? {
    if (last == 0) return null
    val diff = current - last
    val percent = (diff.toFloat() / last.toFloat() * 100).toInt()
    val sign = if (diff >= 0) "+" else ""
    return "$sign$percent% vs Last"
}

fun formatDuration(seconds: Int): String {
    val m = seconds / 60
    val s = seconds % 60
    return String.format("%02d:%02d", m, s)
}
