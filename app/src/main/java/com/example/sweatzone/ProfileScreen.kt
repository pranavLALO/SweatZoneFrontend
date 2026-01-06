package com.example.sweatzone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Message
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.theme.SweatzoneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val profileBgColor = Color(0xFFF3F4F8)
    val cardBgColor = Color.White
    val primaryTextColor = Color(0xFF333333)
    val secondaryTextColor = Color.Gray
    val accentColor = Color(0xFF5A4F7C)

    var userName by remember { mutableStateOf("Koti") }
    var userAge by remember { mutableStateOf("20") }
    var selectedLevel by remember { mutableStateOf("Intermediate") }
    
    var showNameDialog by remember { mutableStateOf(false) }
    var showAgeDialog by remember { mutableStateOf(false) }

    if (showNameDialog) {
        EditInfoDialog(
            title = "Enter your name",
            initialValue = userName,
            onDismiss = { showNameDialog = false },
            onConfirm = { newName ->
                userName = newName
                showNameDialog = false
            }
        )
    }

    if (showAgeDialog) {
        EditInfoDialog(
            title = "Enter your age",
            initialValue = userAge,
            keyboardType = KeyboardType.Number,
            onDismiss = { showAgeDialog = false },
            onConfirm = { newAge ->
                userAge = newAge
                showAgeDialog = false
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Profile", fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = primaryTextColor) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = profileBgColor)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(navController = navController, homeRoute = Screen.BeginnerHome.route)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(profileBgColor)
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = cardBgColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier.size(100.dp).clip(CircleShape).background(accentColor),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(userName.first().toString(), fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        EditableInfoField(label = "NAME", value = userName, modifier = Modifier.weight(1f), onClick = { showNameDialog = true })
                        EditableInfoField(label = "AGE", value = userAge, modifier = Modifier.weight(1f), onClick = { showAgeDialog = true })
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = profileBgColor)
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    ProfileSection(title = "PRIMARY GOAL") {
                        ClickableRow(icon = Icons.Default.FitnessCenter, text = "Muscle Gain")
                    }
                    
                    ProfileSection(title = "BODY METRICS") {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            MetricItem(label = "HEIGHT", value = "175 cm")
                            MetricItem(label = "WEIGHT", value = "75 kg")
                            MetricItem(label = "BMI", value = "24.5")
                        }
                    }
                    
                    ProfileSection(title = "WORKOUT LEVEL") {
                        LevelSelector(selectedLevel = selectedLevel, onLevelSelected = { selectedLevel = it })
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = accentColor, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Member since Jan 2025", color = secondaryTextColor, fontSize = 12.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ActionCard(icon = Icons.Default.Message, text = "Feedback", modifier = Modifier.weight(1f))
                ActionCard(icon = Icons.Default.Email, text = "Contact Us", modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EditableInfoField(label: String, value: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Column(modifier = modifier) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically, 
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(value, fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF333333))
            Icon(Icons.Default.Edit, contentDescription = "Edit $label", tint = Color.Gray, modifier = Modifier.size(16.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFFF0F0F0))
    }
}

@Composable
fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))
        content()
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun ClickableRow(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFFF0F0F0), RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF5A4F7C))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, fontWeight = FontWeight.SemiBold, color = Color(0xFF333333))
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = Color.Gray)
    }
}

@Composable
fun MetricItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 12.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF333333))
    }
}

// --- THIS IS THE NEW, CORRECTED LEVEL SELECTOR ---
@Composable
fun LevelSelector(selectedLevel: String, onLevelSelected: (String) -> Unit) {
    val levels = listOf("Beginner", "Intermediate", "Advanced")
    val selectorBgColor = Color(0xFFF3F4F8)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(selectorBgColor)
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        levels.forEach { level ->
            val isSelected = level == selectedLevel
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(20.dp))
                    .background(if (isSelected) Color.White else Color.Transparent)
                    .border(
                        width = if (isSelected) 1.dp else 0.dp,
                        color = if (isSelected) Color.LightGray.copy(alpha = 0.5f) else Color.Transparent,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .clickable { onLevelSelected(level) }
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = level,
                    color = if (isSelected) Color(0xFF333333) else Color.Gray,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun ActionCard(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF5A4F7C), modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text, color = Color(0xFF333333), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun EditInfoDialog(
    title: String,
    initialValue: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var text by remember { mutableStateOf(initialValue) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
            )
        },
        confirmButton = {
            Button(onClick = { onConfirm(text) }) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    SweatzoneTheme {
        ProfileScreen(rememberNavController())
    }
}