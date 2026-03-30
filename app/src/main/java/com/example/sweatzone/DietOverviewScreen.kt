package com.example.sweatzone

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sweatzone.viewmodel.DietViewModel
import com.example.sweatzone.viewmodel.DietState
import com.example.sweatzone.data.dto.DietMealDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DietOverviewScreen(
    navController: NavController,
    dietViewModel: DietViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {

    // Fetch today's diet when screen opens
    LaunchedEffect(Unit) {
        dietViewModel.getTodayDietPlan(userViewModel.userId)
    }

    val dietState = dietViewModel.dietState.collectAsState().value

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Your Diet Plan",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        when (dietState) {

            // ---------------- LOADING ----------------
            is DietState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            // ---------------- ERROR ----------------
            is DietState.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dietState.message,
                        color = Color.Red
                    )
                }
            }

            // ---------------- SUCCESS ----------------
            is DietState.TodayPlan -> {
                val plan = dietState.data

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {

                    // Loop meal sections (breakfast, lunch, snack, dinner)
                    plan.meals.forEach { (mealTime, mealList) ->

                        // Section Header
                        item {
                            Text(
                                text = mealTime.uppercase(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 10.dp)
                            )
                        }

                        // Meals under section
                        items(mealList) { meal ->
                            MealItem(meal)
                        }
                    }
                }
            }

            else -> {}
        }
    }
}

@Composable
fun MealItem(meal: DietMealDto) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { expanded = !expanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = meal.meal_name,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = meal.meal_type.replaceFirstChar { it.uppercase() },
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                Text(
                    text = "${meal.calories} kcal",
                    fontWeight = FontWeight.Bold
                )
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                Spacer(modifier = Modifier.height(12.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    MacroItem("Protein", "${meal.protein}g", Color(0xFF388E3C))
                    MacroItem("Carbs", "${meal.carbs}g", Color(0xFF1976D2))
                    MacroItem("Fats", "${meal.fats}g", Color(0xFFF57C00))
                }
            }
        }
    }
}

@Composable
fun MacroItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontWeight = FontWeight.Bold, color = color, fontSize = 16.sp)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}
