package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.viewmodel.DietViewModel
import com.example.sweatzone.viewmodel.DietState

@Composable
fun ToolsScreen(
    navController: NavController,
    userViewModel: UserViewModel
) {
    val scrollState = rememberScrollState()

    // ✅ SINGLE ViewModel instance
    val dietViewModel: DietViewModel = viewModel()

    val dietState by dietViewModel.dietState.collectAsState()

    // ✅ SAFE navigation (runs once only)
    LaunchedEffect(dietState) {
        if (dietState is DietState.Success) {
            dietViewModel.resetState()
            navController.navigate("diet_overview") {
                launchSingleTop = true
            }
        }
    }

    Scaffold(
        topBar = { ToolsTopBar(navController) },
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                homeRoute = "tools"
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Select a tool to enhance your training",
                fontSize = 15.sp,
                color = Color.Gray,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // -------- AI FORM CORRECTOR --------
            ToolsCard(
                title = "AI Form Corrector",
                description = "Real-time posture analysis and injury prevention.",
                imageRes = R.drawable.chestimg,
                badgeText = "",
                badgeIcon = Icons.Default.AccessibilityNew,
                buttonText = "Analyze Now",
                buttonIcon = Icons.Default.Videocam,
                onClick = {
                    navController.navigate("video_analysis")
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // -------- SMART DIET PLANNER --------
            ToolsCard(
                title = "Smart Diet Planner",
                description = "Personalized diet plan based on your goal.",
                imageRes = R.drawable.dietimg,
                badgeText = " NUTRITION",
                badgeIcon = Icons.Default.Restaurant,
                buttonText = "Generate Plan",
                buttonIcon = Icons.Default.AutoAwesome,
                onClick = {
                    dietViewModel.generateDietPlan(
                        userId = userViewModel.userId,
                        goal = "muscle_gain" // later replace with saved goal
                    )
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            // -------- STATE UI --------
            when (dietState) {
                is DietState.Loading -> {
                    CircularProgressIndicator()
                }

                is DietState.Error -> {
                    Text(
                        text = (dietState as DietState.Error).message,
                        color = Color.Red
                    )
                }

                else -> Unit
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ToolsTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = {
            Text(
                text = "AI Studio",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    )
}

@Composable
fun ToolsCard(
    title: String,
    description: String,
    imageRes: Int,
    badgeText: String,
    badgeIcon: ImageVector,
    buttonText: String,
    buttonIcon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text(description, color = Color.Gray)
                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = onClick,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(buttonIcon, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(buttonText)
                }
            }
        }
    }
}
