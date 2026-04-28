package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.sweatzone.ui.components.WorkoutCategoryCard
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun BeginnerWorkoutsScreen(navController: NavController) {
    val lavenderBg = Color(0xFFCFAAFF)
    val darkBarColor = Color(0xFF000000)
    val yellowText = Color(0xFFE0FF63)

    val workoutCategories = listOf(
        "Chest" to Screen.BeginnerChestWorkouts.route,
        "Shoulder" to Screen.BeginnerShoulderWorkouts.route,
        "Arms" to Screen.BeginnerArmsWorkouts.route,
        "Legs" to Screen.BeginnerLegsWorkouts.route,
        "Abs" to Screen.BeginnerAbsWorkouts.route,
        "Back" to Screen.BeginnerBackWorkouts.route
    )

    Scaffold(
        bottomBar = {
            AppBottomNavigationBar(navController = navController, homeRoute = Screen.BeginnerHome.route)
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.Warmup.route) },
                containerColor = yellowText,
                contentColor = darkBarColor,
                icon = { Icon(Icons.Filled.PlayArrow, contentDescription = "Start Warmup") },
                text = { Text("Start Warmup", fontWeight = FontWeight.Bold) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(lavenderBg)
                .padding(innerPadding)
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(darkBarColor)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Beginner",
                            color = yellowText,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Workouts",
                            color = yellowText,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Image(
                        painter = painterResource(id = R.drawable.bwtop),
                        contentDescription = "Figure",
                        modifier = Modifier.size(120.dp, 80.dp),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            // Grid Content
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(workoutCategories) { (title, route) ->
                    WorkoutCategoryCard(
                        title = title,
                        imageRes = when(title) {
                            "Chest" -> R.drawable.chestimg
                            "Shoulder" -> R.drawable.shoulderimg
                            "Arms" -> R.drawable.armsimg
                            "Legs" -> R.drawable.legimg
                            "Abs" -> R.drawable.absimg
                            "Back" -> R.drawable.backimg
                            else -> R.drawable.ic_launcher_background // Default image
                        },
                        onClick = { navController.navigate(route) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BeginnerWorkoutsScreenPreview() {
    SweatzoneTheme {
        BeginnerWorkoutsScreen(navController = rememberNavController())
    }
}
