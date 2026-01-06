package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeWorkoutsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Home Workouts", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // You might want to adjust which homeRoute is passed here based on user level
            AppBottomNavigationBar(navController = navController, homeRoute = Screen.BeginnerHome.route)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                WorkoutTypeListItem(
                    title = "Full-Body Home Workouts",
                    description = "Comprehensive routines for overall fitness.",
                    imageRes = R.drawable.chestimg, // Replace with a relevant image
                    onClick = { navController.navigate(Screen.FullBodyHomeWorkouts.route) }
                )
            }
            item { WorkoutTypeListItem(
                title = "Lower Body",
                description = "Strengthen and tone your legs and glutes.",
                imageRes = R.drawable.legimg, // Replace with a relevant image
                onClick = { navController.navigate(Screen.LowerBodyHomeWorkouts.route) }
            ) }
            item { WorkoutTypeListItem(
                title = "Upper Body",
                description = "Build strength in your arms, chest, and back.",
                imageRes = R.drawable.armsimg, // Replace with a relevant image
                onClick = {navController.navigate(Screen.UpperBodyHomeWorkouts.route) }
            ) }
            item { WorkoutTypeListItem(
                title = "Abs",
                description = "Focused exercises for a strong, defined core.",
                imageRes = R.drawable.absimg, // Replace with a relevant image
                onClick = { navController.navigate(Screen.HomeAbsWorkouts.route) } // Navigate to Home Abs Workouts
            ) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WorkoutTypeListItem(
    title: String, 
    description: String, 
    imageRes: Int,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
            )
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
    }
}

@Preview(showBackground = true)
@Composable
fun HomeWorkoutsScreenPreview() {
    SweatzoneTheme {
        HomeWorkoutsScreen(navController = rememberNavController())
    }
}
