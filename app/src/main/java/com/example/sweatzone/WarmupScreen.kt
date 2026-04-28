package com.example.sweatzone

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweatzone.viewmodel.WarmupViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WarmupScreen(
    navController: NavController,
    viewModel: WarmupViewModel = viewModel()
) {
    val currentDegrees by viewModel.currentDegrees.collectAsState()
    val completedReps by viewModel.completedReps.collectAsState()
    val isCalibrating by viewModel.isCalibrating.collectAsState()
    val countdown by viewModel.countdown.collectAsState()
    val isActive by viewModel.isActive.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Warmup Rotation") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Hold your phone securely and rotate your wrist/arm in a full circle.",
                textAlign = TextAlign.Center,
                color = Color.Gray,
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            val warmupType by viewModel.warmupType.collectAsState()

            // Elegant Toggle for Mode Selection
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = Color.LightGray.copy(alpha = 0.2f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
            ) {
                Row(modifier = Modifier.padding(4.dp)) {
                    val isWrist = warmupType == com.example.sweatzone.viewmodel.WarmupType.WRIST
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isWrist) Color.White else Color.Transparent)
                            .clickable(enabled = !isActive) { viewModel.setWarmupType(com.example.sweatzone.viewmodel.WarmupType.WRIST) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Wrist",
                            fontWeight = FontWeight.Bold,
                            color = if (isWrist) Color.Black else Color.Gray,
                            fontSize = 15.sp
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (!isWrist) Color.White else Color.Transparent)
                            .clickable(enabled = !isActive) { viewModel.setWarmupType(com.example.sweatzone.viewmodel.WarmupType.ARM) }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Arm",
                            fontWeight = FontWeight.Bold,
                            color = if (!isWrist) Color.Black else Color.Gray,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(250.dp)
            ) {
                // Background Track
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = Color.LightGray.copy(alpha = 0.3f),
                        style = Stroke(width = 20.dp.toPx())
                    )
                }

                // Progress Arc
                if (isActive && !isCalibrating) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = Color(0xFF4CAF50), // Green for success/progress
                            startAngle = -90f, // Start at the top
                            sweepAngle = currentDegrees.coerceIn(0f, 360f),
                            useCenter = false,
                            style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }

                // Center Content
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    if (!isActive) {
                        Text("Ready", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
                    } else if (isCalibrating) {
                        Text("Hold Still", fontSize = 16.sp, color = Color.Gray)
                        Text("$countdown", fontSize = 48.sp, fontWeight = FontWeight.Bold)
                    } else {
                        Text(
                            text = "$completedReps",
                            fontSize = 64.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFF4CAF50)
                        )
                        Text("Reps", fontSize = 18.sp, color = Color.Gray)
                        
                        // Show current percentage
                        val percentage = ((currentDegrees / 360f) * 100).toInt().coerceIn(0, 100)
                        Text("$percentage%", fontSize = 14.sp, color = Color.LightGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            Button(
                onClick = {
                    if (isActive) viewModel.stopWarmup() else viewModel.startWarmup()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isActive) Color(0xFFF44336) else Color(0xFF2196F3)
                )
            ) {
                Text(
                    text = if (isActive) "Stop Warmup" else "Start Warmup",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
