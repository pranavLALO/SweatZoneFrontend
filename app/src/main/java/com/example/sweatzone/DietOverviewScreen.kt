package com.example.sweatzone

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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

    // Fetch today's diet and water when screen opens
    LaunchedEffect(Unit) {
        dietViewModel.getTodayDietPlan(userViewModel.userId)
        dietViewModel.getWater(userViewModel.userId)
    }

    val dietState = dietViewModel.dietState.collectAsState().value
    val waterGlasses = dietViewModel.waterGlasses.collectAsState().value

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
                val assignedCalories = plan.meals.values.flatten().sumOf { it.calories }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    item {
                        CalorieDashboardCard(
                            targetCalories = plan.total_calories,
                            assignedCalories = assignedCalories
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        WaterTrackerCard(
                            glasses = waterGlasses,
                            onAdd = { dietViewModel.logWater(userViewModel.userId, "add") },
                            onRemove = { dietViewModel.logWater(userViewModel.userId, "remove") }
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                    }

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

@Composable
fun CalorieDashboardCard(targetCalories: Int, assignedCalories: Int) {
    val progress = if (targetCalories > 0) (assignedCalories.toFloat() / targetCalories.toFloat()).coerceIn(0f, 1f) else 0f
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A)) // Premium Dark
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("DAILY TARGET", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "$targetCalories",
                    color = Color.White,
                    fontSize = 36.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text("KCAL", color = Color(0xFFE0FF63), fontSize = 14.sp, fontWeight = FontWeight.Bold)
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text("ASSIGNED TODAY", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "$assignedCalories kcal",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(100.dp)) {
                CircularProgressIndicator(
                    progress = 1f,
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White.copy(alpha = 0.1f),
                    strokeWidth = 12.dp
                )
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE0FF63), // Neon green accent
                    strokeWidth = 12.dp,
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun WaterTrackerCard(glasses: Int, onAdd: () -> Unit, onRemove: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)) // Light blue background
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = androidx.compose.material.icons.Icons.Default.WaterDrop,
                    contentDescription = "Water",
                    tint = Color(0xFF1E88E5),
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text("Daily Hydration", color = Color.Gray, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "$glasses Glasses",
                        color = Color(0xFF1565C0),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
            
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White, androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(androidx.compose.material.icons.Icons.Default.Remove, contentDescription = "Remove", tint = Color(0xFF1E88E5))
                }
                IconButton(
                    onClick = onAdd,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFF1E88E5), androidx.compose.foundation.shape.CircleShape)
                ) {
                    Icon(androidx.compose.material.icons.Icons.Default.Add, contentDescription = "Add", tint = Color.White)
                }
            }
        }
    }
}
