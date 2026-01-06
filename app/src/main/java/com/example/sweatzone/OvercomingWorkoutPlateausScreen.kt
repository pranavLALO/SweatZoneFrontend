package com.example.sweatzone

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OvercomingWorkoutPlateausScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 1. Hero Image Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chestimg), // Use an appropriate image
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient Overlay for text visibility
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White),
                                startY = 300f
                            )
                        )
                )
            }

            // 2. Title & Intro Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-40).dp) // Pull up over the image slightly
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = "Overcoming Workout Plateaus",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    lineHeight = 36.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Break through the barriers holding you back and reignite your fitness progress with proven strategies.",
                    fontSize = 15.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 3. Expandable Strategy Cards

                // Section 1: Identify the Cause
                ExpandableSection(
                    title = "Identify the Cause",
                    subtitle = "Understand why you've stalled.",
                    icon = Icons.Default.Search,
                    iconTint = Color(0xFF7C3AED), // Purple
                    iconBg = Color(0xFFF3E8FF),
                    initiallyExpanded = true
                ) {
                    Column {
                        Text(
                            text = "Progress stalls when your body adapts to stress. To break a plateau, you first need to pinpoint the cause:",
                            fontSize = 14.sp,
                            color = Color(0xFF4B5563),
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        BulletPoint("Inadequate Nutrition", "Are you eating enough to fuel your workouts?")
                        BulletPoint("Insufficient Rest", "Muscle growth happens during recovery, not just in the gym.")
                        BulletPoint("Repetitive Routines", "Doing the same exercises for too long reduces stimulus.")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 2: Progressive Overload
                ExpandableSection(
                    title = "Strategy 1: Progressive Overload",
                    subtitle = "Continuously challenge your muscles.",
                    icon = Icons.Default.TrendingUp,
                    iconTint = Color(0xFF2563EB), // Blue
                    iconBg = Color(0xFFDBEAFE),
                    initiallyExpanded = false
                ) {
                    Text(
                        text = "You must gradually increase the stress placed on your body. Try increasing weight, reps, or sets, or decreasing rest time between sets.",
                        fontSize = 14.sp,
                        color = Color(0xFF4B5563)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 3: Deload Weeks
                ExpandableSection(
                    title = "Strategy 2: Deload Weeks",
                    subtitle = "Give your body a strategic break.",
                    icon = Icons.Default.BatteryChargingFull,
                    iconTint = Color(0xFF059669), // Green
                    iconBg = Color(0xFFD1FAE5),
                    initiallyExpanded = false
                ) {
                    Column {
                        Text(
                            text = "A planned period of reduced training intensity allows your CNS to recover.",
                            fontSize = 14.sp,
                            color = Color(0xFF4B5563)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(text = "How to Implement:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        BulletPoint("Reduce Volume", "Cut sets by 50%.")
                        BulletPoint("Lower Intensity", "Use 50-60% of 1RM.")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Section 4: Vary Your Routine
                ExpandableSection(
                    title = "Strategy 3: Vary Your Routine",
                    subtitle = "Introduce new stimuli.",
                    icon = Icons.Default.Shuffle,
                    iconTint = Color(0xFFD97706), // Orange
                    iconBg = Color(0xFFFEF3C7),
                    initiallyExpanded = false
                ) {
                    Column {
                        BulletPoint("Change Exercises", "Swap bench press for incline dumbbell press.")
                        BulletPoint("Alter Schemes", "Switch from 3x10 to 5x5.")
                        BulletPoint("Reorder Workout", "Start with the exercise you usually do last.")
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        // Back Button Overlay
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.8f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.Black
            )
        }
    }
}

// --- Reusable Components (Keep these in this file if not used elsewhere) ---

@Composable
fun ExpandableSection(
    title: String,
    subtitle: String,
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    initiallyExpanded: Boolean = false,
    content: @Composable () -> Unit
) {
    var expanded by remember { mutableStateOf(initiallyExpanded) }
    val rotationState by animateFloatAsState(targetValue = if (expanded) 180f else 0f)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)), // Very light gray
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(16.dp))
            .clickable { expanded = !expanded }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon Box
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Title Text
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }

                // Arrow
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = Color(0xFF9CA3AF),
                    modifier = Modifier.rotate(rotationState)
                )
            }

            // Expandable Content
            AnimatedVisibility(visible = expanded) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Color(0xFFE5E7EB))
                    Spacer(modifier = Modifier.height(16.dp))
                    content()
                }
            }
        }
    }
}

@Composable
fun BulletPoint(title: String, description: String) {
    Row(modifier = Modifier.padding(bottom = 8.dp)) {
        Text(
            text = "•",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1F2937),
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF374151)
            )
            Text(
                text = description,
                fontSize = 13.sp,
                color = Color(0xFF6B7280),
                lineHeight = 18.sp
            )
        }
    }
}
