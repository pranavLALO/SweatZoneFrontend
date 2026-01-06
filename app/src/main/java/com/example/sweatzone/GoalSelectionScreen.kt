package com.example.sweatzone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.OnboardingProgressBar

@Composable
fun GoalSelectionScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel(),
    fromHome: Boolean = false
) {
    // Specific goals from your image
    val goals = listOf(
        "Strength Training for Muscle Gain",
        "High-Intensity Interval Training for Fat Loss",
        "Cardiovascular Exercise for Fat Loss",
        "Functional Training for Overall Fitness"
    )

    // Default selection
    var selectedGoal by remember { mutableStateOf(goals.first()) }

    val primaryDark = Color(0xFF1C1C1E)
    val checkmarkColor = Color(0xFF1C1C1E)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 1. Progress Bar (Step 3 of 3 visible bars roughly matches your image)
        if (!fromHome) {
            OnboardingProgressBar(currentStep = 3, totalSteps = 3)
        }

        Spacer(modifier = Modifier.height(40.dp))

        // 2. Title and Subtitle
        Text(
            text = "What do you want to\nachieve?",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = primaryDark,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "What you are going to select will\naffect your workout program",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 3. Goal List
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(goals) { goal ->
                GoalOptionItem(
                    text = goal,
                    isSelected = (selectedGoal == goal),
                    onSelect = { selectedGoal = goal }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 4. Footer Buttons (Back + Start Now)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Back Button
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(16.dp))
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Gray)
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Start Now Button
            Button(
                onClick = {
                    userViewModel.updateGoal(selectedGoal)

                    if (fromHome) {
                        navController.popBackStack()
                    }
                    else if (selectedGoal == "Strength Training for Muscle Gain") {
                        // Navigate to specific gender flow if needed
                        navController.navigate(Screen.GenderSelection.route)
                    }
                    else {
                        // Standard flow
                        navController.navigate(Screen.GenderSelection.route)
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryDark),
                modifier = Modifier
                    .height(56.dp)
                    .weight(1f)
            ) {
                Text(
                    text = "Start Now",
                    fontSize = 18.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun GoalOptionItem(
    text: String,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    // Styling based on selection
    val borderColor = if (isSelected) Color(0xFF1C1C1E) else Color(0xFFE0E0E0)
    val borderWidth = if (isSelected) 1.5.dp else 1.dp

    // The main container box
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp) // Taller, pill-shaped cards
            .clip(RoundedCornerShape(20.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(20.dp))
            .background(Color.Transparent)
            .clickable(onClick = onSelect)
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )

        // The Checkmark Icon (visible only when selected)
        if (isSelected) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp) // Offset to sit on the edge
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1C1C1E)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GoalSelectionScreenPreview() {
    GoalSelectionScreen(navController = rememberNavController(), fromHome = false)
}
