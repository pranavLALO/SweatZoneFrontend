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
import androidx.compose.animation.core.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.shape.CircleShape

@Composable
fun WorkoutSummaryScreen(navController: NavController, muscleGroup: String, userViewModel: UserViewModel) {
    val currentResult by userViewModel.currentWorkoutResult.collectAsState()
    val lastWorkoutState by userViewModel.lastWorkout.collectAsState()
    val workoutHistory by userViewModel.workoutHistory.collectAsState()

    LaunchedEffect(Unit) {
        userViewModel.fetchLastWorkout(muscleGroup)
        userViewModel.fetchWorkoutHistory()
    }

    var showSuccessOverlay by remember { mutableStateOf(true) }
    val workoutNumber = (workoutHistory?.size ?: 0) + 1
    val workoutCountText = workoutNumber.toOrdinal()

    val premiumDark = Color(0xFF1A1A1A)
    val premiumAccent = Color(0xFFE0FF63)
    val cardBg = Color(0xFF2C2C2C)

    Box(modifier = Modifier.fillMaxSize()) {
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

                val isBeginner = result.intensity.lowercase() == "beginner" || result.intensity.lowercase() == "easy"

                if (isBeginner) {
                    // Full-width Duration Card
                    SummaryStatCard(
                        modifier = Modifier.fillMaxWidth(),
                        label = "TIME SPENT ON WORKOUT",
                        value = formatDuration(result.totalTimeSeconds),
                        icon = Icons.Default.Timer,
                        accentColor = Color.Cyan
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Motivational Quote Card
                    val quotes = remember {
                        listOf(
                            "The only bad workout is the one that didn't happen. Great job taking the first step today!",
                            "Success isn't always about greatness. It's about consistency. You are on the right path!",
                            "Small daily improvements over time lead to stunning results. Keep going!",
                            "Your body can stand almost anything. It's your mind that you have to convince.",
                            "Believe you can and you're halfway there. You did amazing today!",
                            "Every workout is a step closer to your best self. Keep up the dedication!",
                            "You don't have to be extreme, just consistent. Proud of your effort today!"
                        )
                    }
                    val quote = remember { quotes.random() }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C2C2C)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = premiumAccent.copy(alpha = 0.8f),
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "MOTIVATIONAL FOCUS",
                                color = Color.Gray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "\"$quote\"",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(40.dp))
                } else {
                    // Main Stats Row
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        if (result.intensity.lowercase() == "intermediate" || result.intensity.lowercase() == "medium") {
                            // Calculate progress for intermediate
                            val totalPlanned = result.exerciseLogs.size * 3 * 12 // Default fallback if needed
                            val completionPercent = if (result.totalSets > 0) {
                                (result.totalReps.toFloat() / (result.totalSets * 12).toFloat() * 100).toInt().coerceIn(0, 100)
                            } else {
                                0
                            }
                            SummaryStatCard(
                                modifier = Modifier.weight(1f),
                                label = "GOAL COMPLETION",
                                value = "${completionPercent}%",
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
                }

                // Action Buttons
                Button(
                    onClick = {
                        userViewModel.logWorkout(
                            muscleGroup = result.muscleGroup,
                            intensity = result.intensity,
                            durationSeconds = result.totalTimeSeconds,
                            weightKg = if (result.totalSets > 0) result.totalVolume / result.totalSets else 0, // Avg weight safe
                            completedSets = result.totalSets,
                            completedReps = result.totalReps,
                            timerUsed = result.totalTimeSeconds,
                            exerciseLogs = result.exerciseLogs,
                            onSuccess = {
                                userViewModel.clearCurrentWorkoutResult()
                                val popRoute = when (result.intensity.lowercase()) {
                                    "beginner", "easy" -> Screen.BeginnerHome.route
                                    "intermediate", "medium" -> Screen.IntermediateHome.route
                                    else -> Screen.AdvanceHome.route
                                }
                                navController.popBackStack(popRoute, false)
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

    // Overlay dialog
    if (showSuccessOverlay) {
        SuccessOverlay(
            workoutCountText = workoutCountText,
            onDismiss = {
                showSuccessOverlay = false
            }
        )
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
        if (log.weight_kg > 0) {
            Text(text = "${log.weight_kg} kg", color = Color(0xFFE0FF63), fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
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

fun Int.toOrdinal(): String {
    if (this % 100 in 11..13) {
        return "${this}th"
    }
    return when (this % 10) {
        1 -> "${this}st"
        2 -> "${this}nd"
        3 -> "${this}rd"
        else -> "${this}th"
    }
}

@Composable
fun SuccessOverlay(
    workoutCountText: String,
    onDismiss: () -> Unit
) {
    var animateTrigger by remember { mutableStateOf(false) }
    
    // Trigger animation start
    LaunchedEffect(Unit) {
        animateTrigger = true
    }

    // Scale & Alpha animations for the card
    val scale by animateFloatAsState(
        targetValue = if (animateTrigger) 1f else 0.7f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (animateTrigger) 1f else 0f,
        animationSpec = tween(durationMillis = 300),
        label = "alpha"
    )

    // Infinite transitions for pulsing & rotating the trophy/badge
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )
    val badgeScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "badgeScale"
    )

    // Full screen overlay box
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(enabled = false) {}, // Intercept clicks
        contentAlignment = Alignment.Center
    ) {
        // Premium Success Card
        Card(
            modifier = Modifier
                .width(320.dp)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale,
                    alpha = alpha
                )
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFFE0FF63), Color.Transparent, Color.Cyan)
                    ),
                    shape = RoundedCornerShape(28.dp)
                ),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF242424)),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Trophy/Badge Icon with glowing background and rotation/pulse
                Box(
                    modifier = Modifier
                        .size(120.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Pulsing glow background
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .graphicsLayer(scaleX = badgeScale, scaleY = badgeScale)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        Color(0xFFE0FF63).copy(alpha = 0.3f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                    
                    // Rotating star outline decoration
                    Icon(
                        imageVector = Icons.Default.BrightnessHigh, // Sunburst/Star pattern
                        contentDescription = null,
                        tint = Color(0xFFE0FF63).copy(alpha = 0.2f),
                        modifier = Modifier
                            .size(110.dp)
                            .graphicsLayer(rotationZ = rotation)
                    )
                    
                    // Golden Trophy/Star in the center
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFE0FF63))
                            .graphicsLayer(scaleX = badgeScale, scaleY = badgeScale),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents, // Trophy Icon
                            contentDescription = "Success",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Celebration Header
                Text(
                    text = "WORKOUT COMPLETED!",
                    color = Color(0xFFE0FF63),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dynamic Milestone text
                Text(
                    text = "You completed your\n$workoutCountText workout! 🎉",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Motivational Text
                Text(
                    text = "Awesome job! Keep working out to reach your fitness goals.",
                    color = Color.LightGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                // Return to Home button
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63)),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "SEE PERFORMANCE REPORT",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
