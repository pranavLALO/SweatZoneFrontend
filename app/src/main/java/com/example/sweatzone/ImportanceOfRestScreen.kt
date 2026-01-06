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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.TrendingUp
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
fun ImportanceOfRestScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    // Professional Color Palette
    val textDark = Color(0xFF111827)
    val textGray = Color(0xFF4B5563)
    val textLightGray = Color(0xFF9CA3AF)

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 1. Hero Image
            Box(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.chestimg), // Use an appropriate image
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Bottom Fade for smooth transition
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White),
                                startY = 400f
                            )
                        )
                )
            }

            // 2. Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-20).dp) // Slight overlap for elegance
            ) {
                // Title Section
                Text(
                    text = "The Importance of Rest Days",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark,
                    lineHeight = 32.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "By Dr. Evelyn Reed | 5 min read",
                    fontSize = 13.sp,
                    color = textLightGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Intro Text
                SectionTitle("Why Rest is Crucial")
                BodyText(
                    "When you exercise, you create microscopic tears in your muscle fibers. Rest days allow your body to repair these tears, leading to muscle growth and increased strength. Without adequate rest, you risk overtraining, which can lead to injury and burnout.",
                    textGray
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 3. The Muscle Repair Cycle (Custom Visualization)
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "The Muscle Repair Cycle",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textDark
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CycleItem(
                                icon = Icons.Default.FitnessCenter,
                                color = Color(0xFFDBEAFE), // Blue
                                tint = Color(0xFF2563EB),
                                title = "Workout",
                                sub = "Micro-tears in muscle"
                            )
                            CycleItem(
                                icon = Icons.Default.Bedtime,
                                color = Color(0xFFF3E8FF), // Purple
                                tint = Color(0xFF7C3AED),
                                title = "Rest",
                                sub = "Body repairs fibers"
                            )
                            CycleItem(
                                icon = Icons.Default.TrendingUp,
                                color = Color(0xFFDCFCE7), // Green
                                tint = Color(0xFF16A34A),
                                title = "Growth",
                                sub = "Muscles get stronger"
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 4. Types of Rest
                SectionTitle("Types of Rest: Active vs. Passive")
                BodyText("Not all rest days are created equal. Both active and passive recovery have their place in a balanced fitness routine.", textGray)

                Spacer(modifier = Modifier.height(16.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // Active Rest Card
                    RestTypeCard(
                        modifier = Modifier.weight(1f),
                        title = "Active Rest",
                        desc = "Light activities like walking, yoga, or stretching. Helps increase blood flow and reduce soreness."
                    )
                    // Passive Rest Card
                    RestTypeCard(
                        modifier = Modifier.weight(1f),
                        title = "Passive Rest",
                        desc = "Complete rest from strenuous activity. Crucial for full recovery after intense training periods."
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 5. Benefits Checklist
                SectionTitle("Benefits Beyond Muscle Growth")
                Spacer(modifier = Modifier.height(8.dp))
                BenefitCheckItem("Prevents mental burnout and maintains motivation.")
                BenefitCheckItem("Allows glycogen stores to be fully replenished.")
                BenefitCheckItem("Strengthens the immune system.")

                Spacer(modifier = Modifier.height(32.dp))

                // 6. Conclusion
                SectionTitle("Listen to Your Body")
                BodyText(
                    "Pay attention to signs of overtraining like persistent fatigue, decreased performance, and mood swings. Your body is the best indicator of when you need a break.",
                    textGray
                )

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

// --- Internal Components ---

@Composable
fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF111827),
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
fun BodyText(text: String, color: Color) {
    Text(
        text = text,
        fontSize = 14.sp,
        color = color,
        lineHeight = 22.sp
    )
}

@Composable
fun CycleItem(icon: ImageVector, color: Color, tint: Color, title: String, sub: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(85.dp)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(color),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = tint, modifier = Modifier.size(24.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = title, fontWeight = FontWeight.SemiBold, fontSize = 12.sp, color = Color(0xFF1F2937))
        Text(text = sub, fontSize = 10.sp, color = Color(0xFF6B7280), lineHeight = 12.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
    }
}

@Composable
fun RestTypeCard(modifier: Modifier, title: String, desc: String) {
    Card(
        modifier = modifier.height(180.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)), // Very light gray
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF3F4F6))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = desc, fontSize = 12.sp, color = Color(0xFF4B5563), lineHeight = 16.sp)
        }
    }
}

@Composable
fun BenefitCheckItem(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = Color(0xFF8B5CF6), // Soft Purple
            modifier = Modifier.size(20.dp).padding(top = 2.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(text = text, fontSize = 14.sp, color = Color(0xFF4B5563), lineHeight = 20.sp)
    }
}
