package com.example.sweatzone

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.components.BlogPostCard
import com.example.sweatzone.ui.components.HomeCard
import com.example.sweatzone.ui.components.TopBarSection
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController) {
    val lavenderBg = Color(0xFFE6E0F8)
    val darkPurple = Color(0xFF2C2449)
    val limeText = Color(0xFFDCEB51)

    Scaffold(
        bottomBar = {
            // Assuming the homeRoute for this generic menu is the Beginner Home dashboard
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(lavenderBg)
                .padding(innerPadding)
        ) {
            TopBarSection()

            // This LazyVerticalGrid now acts as your main level selector
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // --- BEGINNER CARD ---
                item {
                    Box(modifier = Modifier.clickable { 
                        com.example.sweatzone.data.local.TokenManager.saveUserLevel("beginner")
                        navController.navigate(Screen.BeginnerHome.route) 
                    }) {
                        HomeCard(
                            title = "Beginner",
                            containerColor = darkPurple,
                            textColor = limeText
                        )
                    }
                }

                // --- INTERMEDIATE CARD ---
                item {
                    Box(modifier = Modifier.clickable { 
                        com.example.sweatzone.data.local.TokenManager.saveUserLevel("intermediate")
                        navController.navigate(Screen.IntermediateHome.route) 
                    }) {
                        HomeCard(
                            title = "Intermediate",
                            containerColor = darkPurple,
                            textColor = limeText
                        )
                    }
                }

                // --- ADVANCE CARD ---
                item {
                    Box(modifier = Modifier.clickable { 
                        com.example.sweatzone.data.local.TokenManager.saveUserLevel("advanced")
                        navController.navigate(Screen.AdvanceHome.route) 
                    }) {
                        HomeCard(
                            title = "Advanced",
                            containerColor = darkPurple,
                            textColor = limeText
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    SweatzoneTheme {
        HomeScreen(navController = rememberNavController())
    }
}
