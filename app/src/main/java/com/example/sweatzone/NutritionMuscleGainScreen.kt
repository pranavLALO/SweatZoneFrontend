package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BreakfastDining
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.MealDto
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun NutritionMuscleGainScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    // Colors from image
    val textDark = Color(0xFF111827)
    val textGray = Color(0xFF4B5563)
    val purplePrimary = Color(0xFF8B5CF6)

    val context = androidx.compose.ui.platform.LocalContext.current
    val scope = rememberCoroutineScope()
    var suggestedMeals by remember { mutableStateOf<List<MealDto>>(emptyList()) }

    fun fetchMeals(isVeg: Boolean) {
        val vegParam = if (isVeg) 1 else 0
        Toast.makeText(context, "Calculating...", Toast.LENGTH_SHORT).show()
        scope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getMeals("muscle_gain", vegParam)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val allMeals = response.body()?.data ?: emptyList()
                        // Select one of each main meal type (exclude snacks for "complete meal" feel)
                        val breakfast = allMeals.filter { it.meal_type == "breakfast" }.randomOrNull()
                        val lunch = allMeals.filter { it.meal_type == "lunch" }.randomOrNull()
                        val dinner = allMeals.filter { it.meal_type == "dinner" }.randomOrNull()
                        
                        suggestedMeals = listOfNotNull(breakfast, lunch, dinner)
                        
                        if (suggestedMeals.isNotEmpty()) {
                            Toast.makeText(context, "Generated balanced meal plan!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "No matching meals found.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                         Toast.makeText(context, "Failed: ${response.message()}", Toast.LENGTH_LONG).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // 1. Hero Image Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.chestimg), 
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                // Gradient
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.White),
                                startY = 300f
                            )
                        )
                )
                // Title
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(
                        text = "Nutrition for Muscle\nGain",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black, 
                        lineHeight = 34.sp
                    )
                }
            }

            // 2. Content Body
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .offset(y = (-10).dp)
            ) {

                // --- Protein Section ---
                NutrientSection(
                    title = "Protein: How Much?",
                    description = "Protein is the building block of muscle. Aim for 1.6–2.2 grams of protein per kilogram of body weight daily to optimize muscle protein synthesis.",
                    cardIcon = Icons.Default.FitnessCenter,
                    cardTitle = "Daily Goal",
                    cardSubtitle = "1.6 - 2.2g per kg",
                    themeColor = Color(0xFF2563EB), // Blue
                    bgColor = Color(0xFFEFF6FF)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Carbs Section ---
                NutrientSection(
                    title = "Carbohydrates: Fuel for Growth",
                    description = "Carbs replenish glycogen stores used during workouts, providing the energy needed for intense training and muscle recovery.",
                    cardIcon = Icons.Default.LocalFireDepartment,
                    cardTitle = "Key Sources",
                    cardSubtitle = "Oats, brown rice, sweet potatoes",
                    themeColor = Color(0xFF16A34A), // Green
                    bgColor = Color(0xFFF0FDF4)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Fats Section ---
                NutrientSection(
                    title = "Healthy Fats: Essential for Hormones",
                    description = "Dietary fats are crucial for producing hormones like testosterone, which plays a key role in muscle growth and repair.",
                    cardIcon = Icons.Default.Science,
                    cardTitle = "Top Choices",
                    cardSubtitle = "Avocado, nuts, olive oil, salmon",
                    themeColor = Color(0xFF7C3AED), // Purple
                    bgColor = Color(0xFFF5F3FF)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- Macro Calculator Card ---
                MacroCalculatorCard(purplePrimary, onCalculate = { isVeg -> fetchMeals(isVeg) })

                Spacer(modifier = Modifier.height(32.dp))

                // --- Sample Meal Ideas ---
                if (suggestedMeals.isNotEmpty()) {
                    Text(
                        text = "Suggested Meals for You",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    suggestedMeals.forEach { meal ->
                        val (icon, color, tint) = when(meal.meal_type) {
                            "breakfast" -> Triple(Icons.Default.BreakfastDining, Color(0xFFDBEAFE), Color(0xFF2563EB))
                            "lunch" -> Triple(Icons.Default.LunchDining, Color(0xFFDCFCE7), Color(0xFF16A34A))
                            "dinner" -> Triple(Icons.Default.DinnerDining, Color(0xFFF3E8FF), Color(0xFF7C3AED))
                            else -> Triple(Icons.Default.LunchDining, Color(0xFFFEF3C7), Color(0xFFD97706))
                        }

                        MealItemCard(
                           title = meal.meal_name,
                           desc = "${meal.calories} kcal | P: ${meal.protein}g C: ${meal.carbs}g F: ${meal.fats}g",
                           icon = icon,
                           iconBg = color,
                           iconTint = tint
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                } else {
                     Text(
                        text = "Sample Meal Ideas",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Fallback static meals
                    MealItemCard(
                        title = "Breakfast Example",
                        desc = "Oatmeal with protein powder and berries",
                        icon = Icons.Default.BreakfastDining,
                        iconBg = Color(0xFFDBEAFE),
                        iconTint = Color(0xFF2563EB)
                    )
                     Spacer(modifier = Modifier.height(12.dp))
                    MealItemCard(
                        title = "Lunch Example",
                        desc = "Chicken breast with brown rice and mixed vegetables",
                        icon = Icons.Default.LunchDining,
                        iconBg = Color(0xFFDCFCE7),
                        iconTint = Color(0xFF16A34A)
                    )
                }

                Spacer(modifier = Modifier.height(50.dp))
            }
        }

        // Back Button
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .padding(top = 48.dp, start = 16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.6f))
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = Color.White
            )
        }
    }
}

// --- Internal Components ---

@Composable
fun NutrientSection(
    title: String,
    description: String,
    cardIcon: ImageVector,
    cardTitle: String,
    cardSubtitle: String,
    themeColor: Color,
    bgColor: Color
) {
    Column {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111827)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = description,
            fontSize = 14.sp,
            color = Color(0xFF4B5563),
            lineHeight = 22.sp
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Info Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(bgColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = cardIcon, contentDescription = null, tint = themeColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = cardTitle, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                    Text(text = cardSubtitle, fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun MacroCalculatorCard(primaryColor: Color, onCalculate: (Boolean) -> Unit) {
    var weightInput by remember { mutableStateOf("") }
    var isVegetarian by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf<String?>(null) }

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5)), // Light Purple/Lavender
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Macro Calculator",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF111827),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Your Weight (kg)", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = weightInput,
                onValueChange = { weightInput = it },
                modifier = Modifier.fillMaxWidth().background(Color.White, RoundedCornerShape(8.dp)),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = primaryColor
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Vegetarian Checkbox
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isVegetarian,
                    onCheckedChange = { isVegetarian = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryColor)
                )
                Text(text = "Vegetarian", fontSize = 14.sp, color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val weight = weightInput.toDoubleOrNull()
                    if (weight != null) {
                        val protein = (weight * 2.0).toInt()
                        val carbs = (weight * 3.5).toInt()
                        val fats = (weight * 0.9).toInt()
                        resultText = "Protein: ${protein}g | Carbs: ${carbs}g | Fats: ${fats}g"
                        onCalculate(isVegetarian)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Calculate My Macros", fontWeight = FontWeight.Bold)
            }

            if (resultText != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = resultText!!,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF4A148C),
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun MealItemCard(title: String, desc: String, icon: ImageVector, iconBg: Color, iconTint: Color) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF111827))
                Text(text = desc, fontSize = 13.sp, color = Color(0xFF6B7280), lineHeight = 18.sp)
            }
        }
    }
}
