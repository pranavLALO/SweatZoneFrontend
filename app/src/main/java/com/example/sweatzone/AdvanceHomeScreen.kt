package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun AdvanceHomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(
                navController = navController,
                homeRoute = Screen.AdvanceHome.route,
                workoutsRoute = Screen.AdvanceWorkouts.route
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            AdvHomeGreeting()
            Spacer(modifier = Modifier.height(24.dp))
            AdvHomeBanner(
                title = "Advance Workouts",
                subtitle = "Push your limits",
                imageRes = R.drawable.chestimg, // Replace with a relevant image
                onClick = { navController.navigate(Screen.AdvanceWorkouts.route) }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AdvStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Goal",
                    mainValue = "Hit The Workout",
                    subValue = "Yearly Progress",
                    icon = Icons.Default.Flag,
                    backgroundColor = Color(0xFFF3E5F5),
                    contentColor = Color(0xFF7B1FA2),
                    onClick = { 
                        val goal = com.example.sweatzone.data.local.TokenManager.getUserGoal()
                        val gender = com.example.sweatzone.data.local.TokenManager.getUserGender()
                        
                        if (goal?.contains("Strength Training", ignoreCase = true) == true) {
                            if (gender == "Male") {
                                navController.navigate(Screen.StrengthTrainingMale.route)
                            } else {
                                navController.navigate(Screen.StrengthTrainingFemale.route)
                            }
                        } else {
                            navController.navigate(Screen.GoalSelection.route)
                        }
                    }
                )
                AdvStatCard(
                    modifier = Modifier.weight(1f),
                    title = "Home\nWorkout",
                    mainValue = "90 min",
                    subValue = "Intense HIIT",
                    icon = Icons.Default.Home,
                    backgroundColor = Color(0xFFE3F2FD),
                    contentColor = Color(0xFF1976D2),
                    onClick = { navController.navigate(Screen.HomeWorkouts.route) }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            AdvBodybuilderSplitsCard(onClick = { navController.navigate(Screen.BodybuilderSplits.route) })
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Pro Tips",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
            AdvBlogPostCard(onClick = { navController.navigate("fitness_blog") })
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- Internal Components for this Screen (Now fully implemented) ---

@Composable
private fun AdvHomeGreeting() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Hi", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(text = "You're on fire!", fontSize = 14.sp, color = Color.Gray)
        }
        Box(
            modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = Icons.Default.Person, contentDescription = "Profile", tint = Color.Black)
        }
    }
}

@Composable
private fun AdvHomeBanner(title: String, subtitle: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth().height(180.dp).clickable(onClick = onClick)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = imageRes), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 100f)))
            Column(modifier = Modifier.align(Alignment.BottomStart).padding(24.dp)) {
                Text(text = title, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text(text = subtitle, color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvStatCard(modifier: Modifier, title: String, mainValue: String, subValue: String, icon: androidx.compose.ui.graphics.vector.ImageVector, backgroundColor: Color, contentColor: Color, onClick: () -> Unit) {
    Card(
        modifier = modifier.height(160.dp), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = backgroundColor), onClick = onClick
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.SpaceBetween) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = contentColor, lineHeight = 20.sp)
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.5f)), contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = contentColor, modifier = Modifier.size(18.dp))
                }
            }
            Column {
                Text(text = mainValue, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = contentColor)
                Text(text = subValue, fontSize = 12.sp, color = contentColor.copy(alpha = 0.7f))
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdvBodybuilderSplitsCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().height(90.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), // Different color
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Elite Bodybuilder", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFFB71C1C))
                Text(text = "LEGACY", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
            }
            Icon(imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color(0xFFB71C1C), modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
private fun AdvBlogPostCard(onClick: () -> Unit) { // Added onClick parameter
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onClick) // Added clickable modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.chestimg),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )
            Text(
                text = "Read Blog",
                color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun AdvanceHomeScreenPreview() {
    SweatzoneTheme {
        AdvanceHomeScreen(rememberNavController())
    }
}