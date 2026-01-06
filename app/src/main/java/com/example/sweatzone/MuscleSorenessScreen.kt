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
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Spa
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun MuscleSorenessScreen(navController: NavController) {
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
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chestimg), // Use a relevant image (e.g., stretching)
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Dark Gradient Overlay for Text Readability
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                startY = 300f
                            )
                        )
                )

                // Title Text
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Dealing with Muscle\nSoreness",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        lineHeight = 34.sp
                    )
                }
            }

            // 2. Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Section: Immediate Relief
                Text(
                    text = "Immediate Relief: Ice vs. Heat",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Confused about whether to use ice or heat? Here's a quick guide to help you decide.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Comparison Row
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ComparisonCard(
                        modifier = Modifier.weight(1f),
                        title = "Use Ice",
                        desc = "For the first 48 hours to reduce inflammation and numb pain.",
                        icon = Icons.Default.AcUnit,
                        bg = Color(0xFFE0F2FE), // Light Blue
                        accent = Color(0xFF0284C7)
                    )
                    ComparisonCard(
                        modifier = Modifier.weight(1f),
                        title = "Use Heat",
                        desc = "After 48 hours to increase blood flow and relax muscles.",
                        icon = Icons.Default.LocalFireDepartment,
                        bg = Color(0xFFFEE2E2), // Light Red
                        accent = Color(0xFFDC2626)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Section: Active Recovery
                Text(
                    text = "Active Recovery Techniques",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Gentle movement can be one of the best ways to ease soreness.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280)
                )

                Spacer(modifier = Modifier.height(16.dp))

                RecoveryTechniqueItem(
                    title = "Light Stretching",
                    desc = "Focus on gentle, static stretches for the sore muscle groups.",
                    icon = Icons.Default.Spa
                )
                Spacer(modifier = Modifier.height(12.dp))
                RecoveryTechniqueItem(
                    title = "Walking or Cycling",
                    desc = "Low-impact cardio boosts circulation without stressing muscles.",
                    icon = Icons.Default.DirectionsRun
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Section: Nutrition
                Text(
                    text = "Nutrition for Recovery",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Green Checklist Box
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDCFCE7)), // Light Green
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Recovery Checklist:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color(0xFF14532D)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        NutritionCheckItem("Protein-rich snack post-workout")
                        NutritionCheckItem("Stay hydrated throughout the day")
                        NutritionCheckItem("Include anti-inflammatory foods")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Section: When to seek help
                Text(
                    text = "When to Seek Help",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111827)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "While soreness is normal, sharp or persistent pain isn't. Consult a professional if you experience pain that lasts more than 72 hours.",
                    fontSize = 14.sp,
                    color = Color(0xFF6B7280),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        // Back Button Overlay
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

// --- Reusable Components ---

@Composable
fun ComparisonCard(modifier: Modifier, title: String, desc: String, icon: ImageVector, bg: Color, accent: Color) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Icon(imageVector = icon, contentDescription = null, tint = accent, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = desc, fontSize = 12.sp, color = Color(0xFF4B5563), lineHeight = 16.sp)
        }
    }
}

@Composable
fun RecoveryTechniqueItem(title: String, desc: String, icon: ImageVector) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF3E8FF)), // Light Purple
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(20.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827))
                Text(text = desc, fontSize = 13.sp, color = Color(0xFF6B7280), lineHeight = 18.sp)
            }
        }
    }
}

@Composable
fun NutritionCheckItem(text: String) {
    Row(modifier = Modifier.padding(vertical = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF22C55E), // Green Check
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = Color(0xFF14532D))
    }
}
