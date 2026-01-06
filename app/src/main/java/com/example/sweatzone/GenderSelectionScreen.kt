package com.example.sweatzone

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun GenderSelectionScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    var selectedGender by remember { mutableStateOf<String?>(null) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Back",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 4.dp).clickable { navController.popBackStack() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(Color(0xFFB39DDB)), // Lavender color
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "What’s Your Gender",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 3. Gender Options
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                GenderOption(
                    label = "Male",
                    isSelected = selectedGender == "Male",
                    gradientColors = listOf(Color(0xFF64B5F6), Color(0xFF4285F4)),
                    symbolContent = { Text("♂", fontSize = 60.sp, color = Color.White, fontWeight = FontWeight.Bold) },
                    onClick = { selectedGender = "Male" }
                )

                Spacer(modifier = Modifier.height(40.dp))

                GenderOption(
                    label = "Female",
                    isSelected = selectedGender == "Female",
                    gradientColors = listOf(Color(0xFFE57373), Color(0xFFEF5350)),
                    symbolContent = { Text("♀", fontSize = 60.sp, color = Color.Black, fontWeight = FontWeight.Bold) },
                    onClick = { selectedGender = "Female" }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // 4. Continue Button
            Button(
                onClick = {
                    if (selectedGender != null) {
                        // Save the gender to the ViewModel
                        userViewModel.setGender(selectedGender!!)
                        // Always navigate to the Physical Activity Level screen next
                        navController.navigate(Screen.PhysicalActivityLevel.route)
                    }
                },
                enabled = selectedGender != null,
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF231F2E)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp, vertical = 32.dp)
                    .height(56.dp)
            ) {
                Text("Continue", fontSize = 18.sp, color = Color.White)
            }
        }
    }
}

@Composable
fun GenderOption(
    label: String,
    isSelected: Boolean,
    gradientColors: List<Color>,
    symbolContent: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(140.dp)
                .shadow(elevation = if (isSelected) 12.dp else 4.dp, shape = CircleShape)
                .clip(CircleShape)
                .background(Brush.verticalGradient(gradientColors))
                .clickable(onClick = onClick)
                .border(
                    width = if (isSelected) 4.dp else 0.dp,
                    color = if (isSelected) Color.White.copy(alpha = 0.5f) else Color.Transparent,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            symbolContent()
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = label,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GenderSelectionPreview() {
    SweatzoneTheme {
        GenderSelectionScreen(navController = rememberNavController())
    }
}