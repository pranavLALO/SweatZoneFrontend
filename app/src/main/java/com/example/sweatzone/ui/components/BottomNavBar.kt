package com.example.sweatzone.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sweatzone.Screen

// Changed from sealed class to data class to allow for dynamic route creation
data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun AppBottomNavigationBar(navController: NavController, homeRoute: String = Screen.BeginnerHome.route) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    // Instantiate items directly, allowing the homeRoute to be dynamic
    // Instantiate items directly, allowing the homeRoute to be dynamic
    val items = listOf(
        BottomNavItem("Home", Icons.Default.Home, homeRoute),
        BottomNavItem("Workouts", Icons.Default.FitnessCenter, Screen.BeginnerWorkouts.route),
        // 👇 UPDATE THIS LINE
        BottomNavItem(" Tools", Icons.Default.SmartToy, "tools"),
        BottomNavItem("Profile", Icons.Default.Person, Screen.Profile.route)
    )


    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}