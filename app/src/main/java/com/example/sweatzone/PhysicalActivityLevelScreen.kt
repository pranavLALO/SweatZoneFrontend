package com.example.sweatzone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun PhysicalActivityLevelScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    var selectedLevel by remember { mutableStateOf("Beginner") }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // This inner column is now scrollable
            Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                 // 2. Title & Subtitle
                Text(
                    text = "Physical Activity\nLevel",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 36.sp,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Choose your current fitness level to\nget personalized recommendations.",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(40.dp))

                // 3. Level Selection Cards
                LevelOptionCard(
                    title = "Beginner",
                    description = "Light activity, just starting out.",
                    isSelected = selectedLevel == "Beginner",
                    onSelect = { selectedLevel = "Beginner" }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LevelOptionCard(
                    title = "Intermediate",
                    description = "Moderate workouts, consistent routine.",
                    isSelected = selectedLevel == "Intermediate",
                    onSelect = { selectedLevel = "Intermediate" }
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                LevelOptionCard(
                    title = "Advanced",
                    description = "Intense training, pushing limits.",
                    isSelected = selectedLevel == "Advanced",
                    onSelect = { selectedLevel = "Advanced" }
                )
            }


            // 4. Continue Button is now always visible at the bottom
            Button(
                onClick = {
                    // Navigate to the appropriate Home Screen based on level
                    when (selectedLevel) {
                        "Beginner" -> navController.navigate(Screen.BeginnerHome.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                        "Intermediate" -> navController.navigate(Screen.IntermediateHome.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                        "Advanced" -> navController.navigate(Screen.AdvanceHome.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                },
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF231F2E) // Dark/Black button
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = "Continue",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun LevelOptionCard(
    title: String,
    description: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val backgroundColor = if (isSelected) Color(0xFF7B61FF) else Color(0xFFF5F0F0)
    val titleColor = if (isSelected) Color.White else Color.Black
    val descriptionColor = if (isSelected) Color.White.copy(alpha = 0.8f) else Color.Gray

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect)
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = titleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = descriptionColor
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PhysicalActivityLevelScreenPreview() {
    SweatzoneTheme {
        PhysicalActivityLevelScreen(navController = rememberNavController())
    }
}