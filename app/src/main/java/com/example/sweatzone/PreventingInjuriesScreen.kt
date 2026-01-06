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
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun PreventingInjuriesScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 1. Hero Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chestimg), // Use an appropriate workout image
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Gradient Overlay
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

            // 2. Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp)
            ) {
                Text(
                    text = "Preventing Common\nWorkout Injuries",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827),
                    lineHeight = 34.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Key practices and techniques to keep you safe and injury-free during training.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Tip 1: Warm-up ---
                InjuryPreventionCard(
                    title = "Warm-up & Cool-down",
                    subtitle = "Prepare your body and aid recovery.",
                    description = "Always start with 5-10 minutes of light cardio and dynamic stretching. Finish with static stretches to improve flexibility.",
                    icon = Icons.Default.DirectionsRun,
                    iconBg = Color(0xFFEFF6FF), // Light Blue
                    iconTint = Color(0xFF2563EB)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Tip 2: Proper Form (With Button) ---
                InjuryPreventionCard(
                    title = "Proper Form is Key",
                    subtitle = "Quality over quantity.",
                    description = "Focus on correct technique for each exercise. If you're unsure, watch tutorials or consult a trainer.",
                    icon = Icons.Default.AccessibilityNew,
                    iconBg = Color(0xFFECFDF5), // Light Green
                    iconTint = Color(0xFF10B981)
                ) {
                    // Custom Button inside card
                    Button(
                        onClick = { /* Navigate to Form Checker tool */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .height(45.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3E8FF)), // Light Purple bg
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(0.dp)
                    ) {
                        Icon(
                            Icons.Default.Checklist,
                            contentDescription = null,
                            tint = Color(0xFF7C3AED),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "View Form Checklist",
                            color = Color(0xFF7C3AED),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- Tip 3: Listen to Body ---
                InjuryPreventionCard(
                    title = "Listen to Your Body",
                    subtitle = "Don't ignore pain signals.",
                    description = "Distinguish between muscle soreness and sharp pain. Rest when needed and don't push through injuries.",
                    icon = Icons.Default.Hearing,
                    iconBg = Color(0xFFF3E8FF), // Light Purple
                    iconTint = Color(0xFF7C3AED)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // --- Tip 4: Cross Training ---
                InjuryPreventionCard(
                    title = "Cross-Training & Mobility",
                    subtitle = "Prevent overuse injuries.",
                    description = "Incorporate different activities to work various muscle groups and improve your range of motion.",
                    icon = Icons.Default.Shuffle,
                    iconBg = Color(0xFFFFFBEB), // Light Yellow
                    iconTint = Color(0xFFF59E0B)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Assess Your Risk Section ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dashedBorder(1.5.dp, Color(0xFFE5E7EB), 24.dp)
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Assess Your Risk",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color(0xFF111827)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Answer a few questions to identify your personal injury risk factors.",
                            fontSize = 13.sp,
                            color = Color(0xFF6B7280),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { /* Open Assessment */ },
                            modifier = Modifier.fillMaxWidth().height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B5CF6)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Start Risk Assessment", fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        // Back Button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

// --- Internal Components ---

@Composable
fun InjuryPreventionCard(
    title: String,
    subtitle: String,
    description: String,
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    content: @Composable (() -> Unit)? = null
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                // Icon
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

                // Header Text
                Column {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF111827)
                    )
                    Text(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color(0xFF6B7280)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Description Text
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF4B5563),
                lineHeight = 22.sp
            )

            // Optional Extra Content (like Buttons)
            content?.invoke()
        }
    }
}

// Helper for Dashed Border (Risk Assessment Section)

