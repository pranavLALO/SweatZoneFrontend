package com.example.sweatzone

import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.viewmodel.ProfileState
import com.example.sweatzone.viewmodel.ProfileViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel()
) {
    val lifecycleOwner = androidx.compose.ui.platform.LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                profileViewModel.loadProfile(userViewModel.userId)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val profileState = profileViewModel.state.collectAsState().value
    val workoutHistory by profileViewModel.workoutHistory.collectAsState()
    
    var selectedPeriod by remember { mutableStateOf("week") }
    var showProgressDetails by remember { mutableStateOf(false) }
    var showSubscriptionDialog by remember { mutableStateOf(false) }

    if (showSubscriptionDialog) {
        androidx.compose.ui.window.Dialog(onDismissRequest = { showSubscriptionDialog = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                shadowElevation = 8.dp
            ) {
                Column {
                    // Premium Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .background(
                                androidx.compose.ui.graphics.Brush.verticalGradient(
                                    colors = listOf(Color(0xFF2D3139), Color(0xFF1A1C20))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("👑", fontSize = 48.sp)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "PREMIUM ACCESS",
                                color = Color.White,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                letterSpacing = 2.sp
                            )
                        }
                    }

                    Column(Modifier.padding(24.dp)) {
                        Text(
                            "Upgrade your fitness journey with exclusive features and advanced tracking.",
                            fontSize = 14.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        
                        Spacer(Modifier.height(24.dp))
                        
                        // Pricing
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Text("₹", fontSize = 24.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 6.dp))
                            Text("100", fontSize = 48.sp, fontWeight = FontWeight.Black)
                            Text("/ month", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(bottom = 8.dp))
                        }

                        Spacer(Modifier.height(32.dp))

                        Text("Secure Payment Via", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Black.copy(alpha = 0.4f))
                        Spacer(Modifier.height(12.dp))
                        
                        // Payment Method Icons / List
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            listOf("UPI", "Cards", "Net").forEach { method ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                        .padding(vertical = 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(method, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.DarkGray)
                                }
                            }
                        }

                        Spacer(Modifier.height(32.dp))

                        // Pay Button
                        Button(
                            onClick = { showSubscriptionDialog = false },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF8294FF), Color(0xFF9E8BFF))
                                    )
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Text("Unlock Now", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(Modifier.height(16.dp))
                        
                        TextButton(
                            onClick = { showSubscriptionDialog = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Not for now", color = Color.Gray)
                        }
                    }
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        },
        containerColor = Color(0xFFFBFBFB) // Slightly warmer white
    ) { padding ->

        if (profileState is ProfileState.Loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else if (profileState is ProfileState.Error) {
             Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Error: ${profileState.message}", color = Color.Red, textAlign = TextAlign.Center, modifier = Modifier.padding(16.dp))
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = {
                            com.example.sweatzone.data.local.TokenManager.clear()
                            navController.navigate(Screen.Login.route) {
                                popUpTo(0) { inclusive = true }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6592C0))
                    ) {
                        Text("Log Out & Reset", color = Color.White)
                    }
                }
            }
        } else if (profileState is ProfileState.Success) {
            val profile = profileState.profile
            val name = profile.name ?: "User"
            val bmi = String.format(Locale.US, "%.1f", profile.weight_kg / ((profile.height_cm / 100f) * (profile.height_cm / 100f)))

            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 24.dp)
            ) {
                // --- CUSTOM HEADER ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // User Avatar
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFFC8CCD9), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name.take(1).uppercase(),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = name,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Profile",
                                    modifier = Modifier.size(18.dp).clickable { /* Edit Logic */ },
                                    tint = Color.Black.copy(alpha = 0.6f)
                                )
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = Color(0xFF5E4997)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "Strength Training • Muscle Gain",
                                    fontSize = 13.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }

                    // Settings Icon
                    IconButton(onClick = { /* Settings Logic */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp),
                            tint = Color.Gray
                        )
                    }
                }

                // Level Badge
                Box(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Surface(
                        color = Color(0xFFFEF3E7),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = profile.activity_level.replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF8B5E3C)
                        )
                    }
                }

                Spacer(Modifier.height(32.dp))

                // --- BODY METRICS CARD ---
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(
                            "BODY METRICS",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(Modifier.height(20.dp))
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            MetricItemTriple("HEIGHT", "${profile.height_cm}", "cm")
                            MetricItemTriple("WEIGHT", "${profile.weight_kg}", "kg")
                            MetricItemTriple("BMI", bmi, "")
                        }
                        
                        Spacer(Modifier.height(24.dp))
                        
                        // View Progress Button
                        Button(
                            onClick = { showProgressDetails = !showProgressDetails },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(
                                    androidx.compose.ui.graphics.Brush.horizontalGradient(
                                        colors = listOf(Color(0xFF8294FF), Color(0xFF9E8BFF))
                                    )
                                ),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            contentPadding = PaddingValues()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    if (showProgressDetails) "Hide Progress" else "View Progress",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(8.dp))
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                // --- CONDITIONAL PROGRESS DETAILS ---
                if (showProgressDetails) {
                    // Weekly Overview Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(0.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("This Week", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text("↑1% vs last week", color = Color(0xFF4CAF50), fontSize = 12.sp)
                            }
                            Spacer(Modifier.height(12.dp))
                            val activeDays = profileState.weeklyStats.count { it.count > 0 }
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text("$activeDays / 4", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                                Spacer(Modifier.width(8.dp))
                                Text("workouts", color = Color.Gray, fontSize = 16.sp, modifier = Modifier.padding(bottom = 2.dp))
                            }
                            
                            Spacer(Modifier.height(16.dp))
                            SegmentedProgressBar(current = activeDays, goal = 4, modifier = Modifier.fillMaxWidth().height(12.dp))
                            
                            Spacer(Modifier.height(16.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Row {
                                    repeat(4) { i ->
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .clip(CircleShape)
                                                .background(if (i < activeDays) Color(0xFF8294FF) else Color(0xFFF3F4F6))
                                                .border(1.dp, if (i < activeDays) Color.Transparent else Color.LightGray.copy(alpha = 0.3f), CircleShape)
                                        )
                                        Spacer(Modifier.width(8.dp))
                                    }
                                }
                                Spacer(Modifier.width(4.dp))
                                Text("${(4 - activeDays).coerceAtLeast(0)} workouts left to hit your goal", color = Color.Gray, fontSize = 12.sp)
                            }
                        }
                    }

                    Spacer(Modifier.height(24.dp))

                    // Consistency Goal/Graph Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Column(Modifier.padding(24.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Consistency Goal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Row {
                                    repeat(3) { i ->
                                        Box(
                                            modifier = Modifier
                                                .size(12.dp)
                                                .clip(CircleShape)
                                                .background(if (i < 1) Color(0xFF8294FF) else Color(0xFFF3F4F6))
                                        )
                                        Spacer(Modifier.width(6.dp))
                                    }
                                }
                            }
                            
                            Spacer(Modifier.height(24.dp))

                            // Tabs
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF3F4F6), RoundedCornerShape(24.dp))
                                    .padding(4.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                listOf("Week", "Month", "Year").forEach { tab ->
                                    val isSelected = tab.lowercase() == selectedPeriod
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(20.dp))
                                            .clickable {
                                                selectedPeriod = tab.lowercase()
                                                profileViewModel.updateStatsPeriod(selectedPeriod)
                                            }
                                            .background(if (isSelected) Color.White else Color.Transparent)
                                            .padding(vertical = 8.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            tab,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            fontSize = 14.sp,
                                            color = if (isSelected) Color.Black else Color.Gray
                                        )
                                    }
                                }
                            }

                            Spacer(Modifier.height(24.dp))
                            WorkoutGraphV2(profileState.graphStats, selectedPeriod)
                        }
                    }
                    
                    Spacer(Modifier.height(24.dp))
                }

                // --- CUSTOM WORKOUTS (NEW FEATURE) ---
                val customRoutines by profileViewModel.customRoutines.collectAsState()

                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "MY CUSTOM WORKOUTS",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    TextButton(onClick = { navController.navigate("workout_library") }) {
                        Text("+ BUILD NEW", color = Color(0xFF6592C0), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }

                if (customRoutines.isNotEmpty()) {
                    androidx.compose.foundation.lazy.LazyRow(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(customRoutines.size) { index ->
                            val routine = customRoutines[index]
                            Card(
                                modifier = Modifier
                                    .width(200.dp)
                                    .clickable { navController.navigate("custom_workout/${routine.id}") },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(
                                        text = routine.routine_name,
                                        color = Color(0xFFE0FF63),
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        maxLines = 1,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "${routine.exercises.size} Exercises",
                                        color = Color.White,
                                        fontSize = 14.sp
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Check, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Play Now", color = Color.Gray, fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp).clickable { navController.navigate("workout_library") },
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFFE0FF63).copy(alpha = 0.5f))
                    ) {
                        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                            Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Color(0xFFE0FF63))
                            Spacer(Modifier.width(8.dp))
                            Text("Create your first Custom Routine!", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // --- RECENT WORKOUTS HISTORY ---
                if (workoutHistory.isNotEmpty()) {
                    Text(
                        text = "RECENT SESSIONS",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    workoutHistory.forEach { historyItem ->
                        WorkoutHistoryCard(
                            item = historyItem,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    Spacer(Modifier.height(16.dp))
                }

                // --- GO PREMIUM BANNER (Simplified) ---
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showSubscriptionDialog = true },
                    shape = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                androidx.compose.ui.graphics.Brush.linearGradient(
                                    colors = listOf(Color(0xFF1A1C20), Color(0xFF2D3139))
                                )
                            )
                            .padding(24.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("👑", fontSize = 24.sp)
                                Spacer(Modifier.width(12.dp))
                                Text(
                                    "Subscribe",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))

                // Logout Button
                TextButton(
                    onClick = {
                        com.example.sweatzone.data.local.TokenManager.clear()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Log Out", color = Color.Gray, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@Composable
fun MetricItemTriple(label: String, value: String, unit: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(6.dp))
        Row(verticalAlignment = Alignment.Bottom) {
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            if (unit.isNotEmpty()) {
                Spacer(Modifier.width(4.dp))
                Text(unit, fontSize = 16.sp, color = Color.Black, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 2.dp))
            }
        }
    }
}

@Composable
fun MetricItemV2(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun WorkoutGraphV2(stats: List<com.example.sweatzone.data.dto.WorkoutStat>, period: String) {
    if (stats.isEmpty()) {
        Box(Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
            Text("No data", color = Color.Gray)
        }
        return
    }

    val maxVal = 45f 
    // Elegant gradient for bars
    val barBrush = androidx.compose.ui.graphics.Brush.verticalGradient(
        colors = listOf(
            Color(0xFF6592C0),
            Color(0xFF8AB6E1)
        )
    )
    val gridColor = Color.LightGray.copy(alpha = 0.3f) // Subtler grid

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp) // Slightly taller for elegance
            .padding(top = 24.dp, bottom = 24.dp, end = 24.dp)
    ) {
        val width = size.width
        val height = size.height
        val barAreaHeight = height
        
        // Draw Grid Lines & Y-Axis Labels
        val steps = 3
        val stepHeight = barAreaHeight / steps
        val stepValue = 15 

        for (i in 0..steps) {
            val y = barAreaHeight - (i * stepHeight)
            
            // Finer Dotted Line
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(5f, 5f), 0f),
                strokeWidth = 2f
            )

            // Refined Y-Axis Label
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "${i * stepValue}",
                    width + 12f,
                    y + 10f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY // Professional Gray
                        textSize = 24f // Slightly smaller for precision
                        typeface = android.graphics.Typeface.create(android.graphics.Typeface.DEFAULT, android.graphics.Typeface.NORMAL)
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
            }
        }

        // Draw Bars & X-Axis Labels
        val availableWidth = width
        val count = stats.size.coerceAtLeast(1)
        
        // Better bar width sizing
        val maxBarWidth = 60f 
        val computedBarWidth = when (period) {
            "month" -> (availableWidth / count) * 0.7f 
            "year" -> (availableWidth / count) * 0.5f
            else -> (availableWidth / count) * 0.5f 
        }
        val barWidth = computedBarWidth.coerceAtMost(maxBarWidth) 
        
        // Robust spacing calculation
        val totalBarWidth = barWidth * count
        val spacing = if (count > 1) (availableWidth - totalBarWidth) / (count - 1) else 0f

        stats.forEachIndexed { index, stat ->
            val x = index * (barWidth + spacing)
            
            // Draw Bar
            val barHeight = (stat.count / maxVal).coerceIn(0f, 1f) * barAreaHeight
            val top = barAreaHeight - barHeight
            
            val barPath = androidx.compose.ui.graphics.Path().apply {
                addRoundRect(
                    androidx.compose.ui.geometry.RoundRect(
                        left = x,
                        top = top,
                        right = x + barWidth,
                        bottom = barAreaHeight,
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(6f, 6f) // Modern sharp-rounded corners
                    )
                )
            }
            drawPath(path = barPath, brush = barBrush)

            // Draw X-Axis Label logic
            val label = when (period) {
                "week" -> stat.day // Already "Mon", "Tue" etc. from backend
                "month" -> {
                    // Show every 5th label or first/last to avoid overlap
                    if (index == 0 || index == stats.size - 1 || (stat.day.toIntOrNull() ?: 0) % 5 == 0) stat.day else ""
                }
                "year" -> stat.day.take(3) // "Jan", "Feb"
                else -> stat.day.take(1)
            }
            
            if (label.isNotEmpty()) {
                drawContext.canvas.nativeCanvas.apply {
                    drawText(
                        label,
                        x + (barWidth / 2),
                        height + 40f, 
                        android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 26f
                            typeface = android.graphics.Typeface.DEFAULT
                            textAlign = android.graphics.Paint.Align.CENTER
                        }
                    )
                }
            }
        }
        
        // Goal Line
        val goalY = barAreaHeight - ((28f / maxVal) * barAreaHeight)
        drawLine(
            color = Color(0xFF6592C0).copy(alpha = 0.8f),
            start = Offset(0f, goalY),
            end = Offset(width, goalY),
            strokeWidth = 3f,
            pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(20f, 10f), 0f)
        )
        // Goal Label
         drawContext.canvas.nativeCanvas.apply {
                drawText(
                    "28",
                    width + 12f, 
                    goalY + 10f,
                    android.graphics.Paint().apply {
                        color = android.graphics.Color.parseColor("#6592C0")
                        textSize = 28f
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                        textAlign = android.graphics.Paint.Align.LEFT
                    }
                )
            }
    }
}

@Composable
fun SegmentedProgressBar(
    current: Int,
    goal: Int,
    modifier: Modifier = Modifier
) {
    val shouldUseSegments = goal <= 60 

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        if (shouldUseSegments) {
            val gap = 4f // Gap between segments
            val totalGap = gap * (goal - 1)
            // Ensure segmentWidth is at least something positive
            val segmentWidth = ((width - totalGap) / goal).coerceAtLeast(1f)
            
            for (i in 0 until goal) {
                val left = i * (segmentWidth + gap)
                
                // Active or Inactive Color
                val color = if (i < current) {
                     Color(0xFF6592C0) 
                } else {
                     Color(0xFFE5E9F0) 
                }

                // Draw Rect
                drawRoundRect(
                    color = color,
                    topLeft = Offset(left, 0f),
                    size = Size(segmentWidth, height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(height / 2, height / 2)
                )
            }
        } else {
            // Continuous bar for Year view (Background)
             drawRoundRect(
                color = Color(0xFFE5E9F0),
                size = size,
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(height / 2, height / 2)
            )
            
            // Foreground Progress
            val progressWidth = (current / goal.toFloat()).coerceIn(0f, 1f) * width
             drawRoundRect(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                    colors = listOf(Color(0xFF6592C0), Color(0xFF4A76A8))
                ),
                size = Size(progressWidth, height),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(height / 2, height / 2)
            )
        }
    }
}

@Composable
fun WorkoutHistoryCard(
    item: com.example.sweatzone.data.dto.WorkoutHistoryItem,
    modifier: Modifier = Modifier
) {
    val quotes = listOf(
        "Train insane or remain the same.",
        "Discipline equals freedom.",
        "Don't stop when you're tired. Stop when you're done.",
        "No pain, no gain. Shut up and train.",
        "Your body can stand almost anything. It's your mind you have to convince.",
        "Wake up. Work out. Look kick-ass.",
        "Sore today, strong tomorrow.",
        "Sweat is just fat crying.",
        "Excuses don't burn calories.",
        "Push harder than yesterday if you want a different tomorrow."
    )
    val quote = quotes[item.id % quotes.size]
    val rawDate = item.completed_at
    val displayDate = if (rawDate.length >= 10) rawDate.substring(0, 10) else rawDate

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFE0FF63).copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FitnessCenter,
                            contentDescription = "Workout",
                            tint = Color(0xFFE0FF63),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text(
                            text = item.muscle_group.replaceFirstChar { it.uppercase() },
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            text = displayDate,
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                    }
                }
                
                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    val volume = item.weight_kg * item.completed_reps * item.completed_sets
                    Text(
                        text = "$volume kg Vol",
                        color = Color(0xFFE0FF63),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                HistoryMiniStat("SETS", "${item.completed_sets}", Color.Cyan)
                HistoryMiniStat("REPS", "${item.completed_reps}", Color.Magenta)
                HistoryMiniStat("WEIGHT", "${item.weight_kg} kg", Color(0xFFFF9800))
                val min = item.duration_seconds / 60
                val sec = item.duration_seconds % 60
                HistoryMiniStat("TIME", String.format(java.util.Locale.US, "%02d:%02d", min, sec), Color.Red)
            }
            
            Spacer(Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.White.copy(alpha = 0.1f)))
            Spacer(Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ChatBubble,
                    contentDescription = null,
                    tint = Color.Gray.copy(alpha = 0.5f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "\"$quote\"",
                    color = Color.Gray,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun HistoryMiniStat(label: String, value: String, dotColor: Color) {
    Column(horizontalAlignment = Alignment.Start) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(6.dp).background(dotColor, CircleShape))
            Spacer(Modifier.width(4.dp))
            Text(label, fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(4.dp))
        Text(value, fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
    }
}
