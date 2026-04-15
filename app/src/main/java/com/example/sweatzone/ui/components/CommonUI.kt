package com.example.sweatzone.ui.components

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.sweatzone.R
import com.example.sweatzone.Screen

// --- ADDED BACK MISSING COMPOSABLES ---

@Composable
fun TopBarSection() {
    val darkBg = Color(0xFF2C2449)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(darkBg)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hi",
                color = Color(0xFFB39DDB),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "It's time to challenge your limits.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color(0xFFB39DDB),
            modifier = Modifier.size(32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCard(
    title: String,
    containerColor: Color,
    textColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(24.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            Text(
                text = title,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun BlogPostCard() { // Simplified to not require parameters for now
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.chestimg), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 100f)))
            Text(
                text = "Mastering the Basics: A Guide to\nPerfect Push-Ups",
                color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)
            )
        }
    }
}

// --- END OF ADDED COMPOSABLES ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCategoryCard(
    title: String,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ExerciseItem(
    title: String,
    videoResId: Int = 0,
    videoUrl: String? = null,
    instructions: List<String>,
    benefits: List<String>,
    trackingMode: String = "none",
    plannedSets: Int = 3,
    plannedReps: Int = 12,
    onDataChanged: (completedSets: Int, totalReps: Int) -> Unit = { _, _ -> }
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Embedded Video Player ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        try {
                            if (!videoUrl.isNullOrEmpty()) {
                                val mediaItem = MediaItem.fromUri(Uri.parse(videoUrl))
                                setMediaItem(mediaItem)
                                repeatMode = Player.REPEAT_MODE_ONE // Loop the video
                                playWhenReady = true
                                prepare()
                                volume = 0f // Start muted for autoplay UX
                            } else if (videoResId != 0) { // Check for a valid resource ID
                                val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                                val mediaItem = MediaItem.fromUri(videoUri)
                                setMediaItem(mediaItem)
                                repeatMode = Player.REPEAT_MODE_ONE // Loop the video
                                playWhenReady = true
                                prepare()
                                volume = 0f // Start muted for autoplay UX
                            } else {
                                // Handle invalid videoResId, maybe show a placeholder or log an error
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Instructions ---
            Text("Instructions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            instructions.forEachIndexed { index, instruction ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("${index + 1}. ", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
                    Text(instruction, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Benefits ---
            Text("Benefits", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            benefits.forEach { benefit ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
                    Text(benefit, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
            }
            if (trackingMode != "none") {
                Spacer(modifier = Modifier.height(24.dp))
                when (trackingMode) {
                    "advanced" -> {
                        PremiumAdvancedWorkoutTracker(
                            targetSets = plannedSets,
                            onExerciseSave = { s, r, w, t -> 
                                onDataChanged(s, r) // Simplified bridge
                            }
                        )
                    }
                    "intermediate" -> {
                        IntermediateWorkoutTracker(
                            targetSets = plannedSets,
                            targetReps = plannedReps,
                            onExerciseSave = { s, r ->
                                onDataChanged(s, r)
                            }
                        )
                    }
                    "basic" -> {
                        SetsAndRepsTracker(targetSets = plannedSets, targetRepsText = "$plannedReps")
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumAdvancedWorkoutTracker(
    targetSets: Int = 3,
    onExerciseSave: (sets: Int, reps: Int, weight: Int, timeUsed: Int) -> Unit
) {
    val selectedSet = remember { androidx.compose.runtime.mutableStateOf(1) }
    val repsMap = remember { androidx.compose.runtime.mutableStateMapOf<Int, Int>().apply { 
        (1..targetSets).forEach { this[it] = 10 } 
    } }
    val weightMap = remember { androidx.compose.runtime.mutableStateMapOf<Int, Int>().apply { 
        (1..targetSets).forEach { this[it] = 20 } 
    } }
    val timeMap = remember { androidx.compose.runtime.mutableStateMapOf<Int, Int>().apply { 
        (1..targetSets).forEach { this[it] = 0 } 
    } }
    val timeRemainingMap = remember { androidx.compose.runtime.mutableStateMapOf<Int, Int>().apply { 
        (1..targetSets).forEach { this[it] = 180 } 
    } }
    val timeRunningMap = remember { androidx.compose.runtime.mutableStateMapOf<Int, Boolean>().apply { 
        (1..targetSets).forEach { this[it] = false } 
    } }

    // Premium UI Colors
    val premiumDark = Color(0xFF1A1A1A)
    val premiumAccent = Color(0xFFE0FF63)
    val glassWhite = Color.White.copy(alpha = 0.1f)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = premiumDark),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Advanced Tracking",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                
                // Volume Badge
                Surface(
                    color = premiumAccent,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val currentWeight = weightMap[selectedSet.value] ?: 0
                    val currentReps = repsMap[selectedSet.value] ?: 0
                    Text(
                        text = "${currentWeight * currentReps} kg Vol",
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Set Selector (Horizontal Pill)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..targetSets).forEach { setNum ->
                    val isSelected = selectedSet.value == setNum
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) premiumAccent else glassWhite)
                            .clickable { selectedSet.value = setNum }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SET $setNum",
                            color = if (isSelected) Color.Black else Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reps & Weight Controls
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Reps Column
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("REPS", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { 
                            val c = repsMap[selectedSet.value] ?: 10
                            if (c > 1) repsMap[selectedSet.value] = c - 1
                        }) { Icon(Icons.Default.Remove, "", tint = Color.White) }
                        Text("${repsMap[selectedSet.value]}", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        IconButton(onClick = { 
                            val c = repsMap[selectedSet.value] ?: 10
                            repsMap[selectedSet.value] = c + 1
                        }) { Icon(Icons.Default.Add, "", tint = Color.White) }
                    }
                }

                // Weight Column (Dropdown Simulation)
                var showWeightMenu by remember { androidx.compose.runtime.mutableStateOf(false) }
                val weights = listOf(10, 15, 20, 25, 30, 35, 40, 45, 50)
                
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("WEIGHT (KG)", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Box {
                        Text(
                            text = "${weightMap[selectedSet.value]} kg",
                            color = premiumAccent,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable { showWeightMenu = true }
                                .padding(vertical = 8.dp)
                        )
                        DropdownMenu(
                            expanded = showWeightMenu,
                            onDismissRequest = { showWeightMenu = false },
                            modifier = Modifier.background(Color(0xFF333333))
                        ) {
                            weights.forEach { w ->
                                DropdownMenuItem(
                                    text = { Text("$w kg", color = Color.White) },
                                    onClick = {
                                        weightMap[selectedSet.value] = w
                                        showWeightMenu = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 180s Disciplined Timer
            CountdownTimer(
                timeLeft = timeRemainingMap[selectedSet.value] ?: 180,
                isRunning = timeRunningMap[selectedSet.value] ?: false,
                onTimeLeftChange = { timeRemainingMap[selectedSet.value] = it },
                onIsRunningChange = { timeRunningMap[selectedSet.value] = it },
                onTimerFinished = { actualSeconds ->
                    timeMap[selectedSet.value] = (timeMap[selectedSet.value] ?: 0) + actualSeconds
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            val context = androidx.compose.ui.platform.LocalContext.current
            
            // Lock Exercise Button
            Button(
                onClick = {
                    val totalSets = targetSets
                    val avgReps = repsMap.values.sum() / targetSets
                    val avgWeight = weightMap.values.sum() / targetSets
                    val totalTime = timeMap.values.sum()
                    onExerciseSave(totalSets, avgReps, avgWeight, totalTime)
                    android.widget.Toast.makeText(context, "Exercise Data Locked \uD83D\uDD12", android.widget.Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF333333)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = premiumAccent)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Lock Exercise Data", color = Color.White)
            }
        }
    }
}

@Composable
fun CountdownTimer(
    timeLeft: Int,
    isRunning: Boolean,
    onTimeLeftChange: (Int) -> Unit,
    onIsRunningChange: (Boolean) -> Unit,
    onTimerFinished: (Int) -> Unit
) {
    val totalTime = 180

    androidx.compose.runtime.LaunchedEffect(isRunning, timeLeft) {
        if (isRunning && timeLeft > 0) {
            kotlinx.coroutines.delay(1000L)
            onTimeLeftChange(timeLeft - 1)
            if (timeLeft - 1 == 0) {
                onIsRunningChange(false)
                onTimerFinished(totalTime)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "REST TIMER",
            color = Color.Gray,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = String.format("%02d:%02d", timeLeft / 60, timeLeft % 60),
            color = if (timeLeft < 30) Color.Red else Color.White,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold
        )

        LinearProgressIndicator(
            progress = timeLeft.toFloat() / totalTime.toFloat(),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = if (timeLeft < 30) Color.Red else Color(0xFFE0FF63),
            trackColor = Color.White.copy(alpha = 0.1f)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { 
                    if (isRunning) {
                        onTimerFinished(totalTime - timeLeft)
                        onIsRunningChange(false)
                    } else {
                        onIsRunningChange(true)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) Color.Red.copy(alpha = 0.2f) else Color(0xFFE0FF63).copy(alpha = 0.2f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (isRunning) "STOP" else "START REST",
                    color = if (isRunning) Color.Red else Color(0xFFE0FF63),
                    fontWeight = FontWeight.Bold
                )
            }
            
            TextButton(onClick = { 
                onTimeLeftChange(180)
                onIsRunningChange(false)
            }) {
                Text("RESET", color = Color.Gray)
            }
        }
    }
}

@Composable
fun IntermediateWorkoutTracker(
    targetSets: Int = 3,
    targetReps: Int = 12,
    onExerciseSave: (completedSets: Int, totalReps: Int) -> Unit
) {
    val selectedSet = remember { androidx.compose.runtime.mutableStateOf(1) }
    val repsMap = remember { androidx.compose.runtime.mutableStateMapOf<Int, Int>().apply { 
        (1..targetSets).forEach { this[it] = targetReps } 
    } }
    val isLocked = remember { androidx.compose.runtime.mutableStateOf(false) }

    // Consistent Intermediate Colors
    val softLavender = Color(0xFFF3E5F5)
    val deepPurple = Color(0xFF7B1FA2)
    val successGreen = Color(0xFF43A047)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = softLavender)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Track Performance",
                    color = deepPurple,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                val totalRepsDone = repsMap.values.sum()
                val totalPlanned = targetSets * targetReps
                val progress = (totalRepsDone.toFloat() / totalPlanned.toFloat()).coerceIn(0f, 1f)
                
                Surface(
                    color = if (progress >= 1f) successGreen else deepPurple,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${(progress * 100).toInt()}% Goal",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Set Pills
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                (1..targetSets).forEach { setNum ->
                    val isSelected = selectedSet.value == setNum
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) deepPurple else Color.White)
                            .clickable { selectedSet.value = setNum }
                            .padding(vertical = 10.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SET $setNum",
                            color = if (isSelected) Color.White else deepPurple,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Reps Control
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { 
                        val c = repsMap[selectedSet.value] ?: targetReps
                        if (c > 0) repsMap[selectedSet.value] = c - 1
                    },
                    enabled = !isLocked.value,
                    modifier = Modifier.background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Remove, "Less", tint = deepPurple)
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${repsMap[selectedSet.value]}",
                        color = deepPurple,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "REPS DONE",
                        color = Color.Gray,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { 
                        val c = repsMap[selectedSet.value] ?: targetReps
                        repsMap[selectedSet.value] = c + 1
                    },
                    enabled = !isLocked.value,
                    modifier = Modifier.background(Color.White, CircleShape)
                ) {
                    Icon(Icons.Default.Add, "More", tint = deepPurple)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isLocked.value = true
                    onExerciseSave(targetSets, repsMap.values.sum())
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isLocked.value) successGreen else deepPurple
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = if (isLocked.value) Icons.Default.CheckCircle else Icons.Default.Lock,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    if (isLocked.value) "PERFORMANCE SAVED" else "LOCK SET DATA",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun SetsAndRepsTracker(targetSets: Int = 3, targetRepsText: String = "10-12") {
    androidx.compose.material3.Text(
        text = "Sets: $targetSets | Reps: $targetRepsText",
        fontSize = 16.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
        color = androidx.compose.ui.graphics.Color(0xFF1A1A1A)
    )
}
