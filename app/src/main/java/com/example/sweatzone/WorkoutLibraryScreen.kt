package com.example.sweatzone

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.SaveCustomRoutineRequest
import com.example.sweatzone.data.dto.WorkoutExerciseDto
import com.example.sweatzone.ui.components.ExerciseItem
import com.example.sweatzone.ui.theme.SweatzoneTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true)
@Composable
fun WorkoutLibraryScreenPreview() {
    SweatzoneTheme {
        WorkoutLibraryScreen(
            navController = rememberNavController(),
            userViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutLibraryScreen(navController: NavController, userViewModel: UserViewModel) {
    var allExercises by remember { mutableStateOf<List<WorkoutExerciseDto>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val muscleGroups = listOf("chest", "arms", "legs", "shoulder", "back", "abs")
    
    val selectedExercises = remember { mutableStateListOf<WorkoutExerciseDto>() }
    var isCreationMode by remember { mutableStateOf(false) }
    var showNameDialog by remember { mutableStateOf(false) }
    var routineName by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    
    // Track expanded muscle groups
    val expandedGroups = remember { mutableStateMapOf<String, Boolean>() }

    LaunchedEffect(Unit) {
        userViewModel.fetchCustomRoutines() // Pre-fetch for the carousel screen later
        try {
            val response = RetrofitClient.api.getLibrary()
            if (response.isSuccessful && response.body()?.status == true) {
                allExercises = response.body()?.data ?: emptyList()
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load library", Toast.LENGTH_SHORT).show()
        }
        isLoading = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout Library", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    // Shortcut to My Custom Plans
                    IconButton(onClick = { navController.navigate("custom_plans") }) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = "My Plans",
                            tint = Color(0xFFE0FF63)
                        )
                    }
                    IconButton(onClick = { navController.navigate(Screen.PersonalizedProgress.route) }) {
                        Icon(
                            imageVector = Icons.Default.Analytics,
                            contentDescription = "Progress Report",
                            tint = Color(0xFFE0FF63)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        floatingActionButton = {
            // Only show FAB if NOT in creation mode AND at least one group is expanded
            if (!isCreationMode && expandedGroups.any { it.value == true }) {
                ExtendedFloatingActionButton(
                    text = { Text("CREATE PLAN", color = Color.Black, fontWeight = FontWeight.ExtraBold) },
                    icon = { Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black) },
                    onClick = { isCreationMode = true },
                    containerColor = Color(0xFFE0FF63),
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        bottomBar = {
            if (isCreationMode) {
                Surface(
                    color = Color(0xFF1E1E1E),
                    shadowElevation = 12.dp,
                    tonalElevation = 8.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (selectedExercises.isEmpty()) "Select Exercises" else "${selectedExercises.size} Selected",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            if (selectedExercises.isNotEmpty()) {
                                Text(
                                    text = "Ready to build plan",
                                    color = Color(0xFFE0FF63),
                                    fontSize = 12.sp
                                )
                            }
                        }
                        Row {
                            TextButton(onClick = { 
                                isCreationMode = false
                                selectedExercises.clear()
                            }) {
                                Text("CANCEL", color = Color.Gray)
                            }
                            if (selectedExercises.isNotEmpty()) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Button(
                                    onClick = { showNameDialog = true },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("FINISH & NAME", color = Color.Black, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = Color(0xFFE0FF63))
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 80.dp), // Space for bottom bar
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    muscleGroups.forEach { muscleGroup ->
                        val groupExercises = allExercises.filter { it.target_muscle.equals(muscleGroup, ignoreCase = true) }
                        
                        if (groupExercises.isNotEmpty()) {
                            val isExpanded = expandedGroups[muscleGroup] ?: false
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFE0FF63).copy(alpha = 0.1f))
                                        .clickable { 
                                            expandedGroups[muscleGroup] = !isExpanded 
                                        }
                                        .padding(16.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = muscleGroup.uppercase(),
                                            color = Color(0xFFE0FF63),
                                            fontWeight = FontWeight.ExtraBold,
                                            fontSize = 20.sp,
                                            letterSpacing = 2.sp
                                        )
                                        Icon(
                                            imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = "Expand",
                                            tint = Color(0xFFE0FF63)
                                        )
                                    }
                                }
                            }

                            if (isExpanded) {
                                items(groupExercises) { exercise ->
                                    val isSelected = selectedExercises.any { it.id == exercise.id }
                                    
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp)
                                            .then(
                                                if (isSelected) Modifier.border(2.dp, Color(0xFFE0FF63), RoundedCornerShape(16.dp))
                                                else Modifier
                                            )
                                            .clickable(enabled = isCreationMode) {
                                                if (isSelected) {
                                                    selectedExercises.removeAll { it.id == exercise.id }
                                                } else {
                                                    selectedExercises.add(exercise)
                                                }
                                            }
                                    ) {
                                        ExerciseItem(
                                            title = exercise.title,
                                            videoUrl = "${RetrofitClient.BASE_URL}videos/${exercise.video_filename}",
                                            instructions = exercise.instructions,
                                            benefits = exercise.benefits,
                                            trackingMode = "none"
                                        )
                                        
                                        if (isCreationMode) {
                                            // Selection Overlay Toggle Bubble
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .padding(16.dp)
                                                    .size(40.dp)
                                                    .clip(androidx.compose.foundation.shape.CircleShape)
                                                    .background(if (isSelected) Color(0xFFE0FF63) else Color.White.copy(alpha = 0.2f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                                                    contentDescription = "Select",
                                                    tint = if (isSelected) Color.Black else Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showNameDialog) {
        AlertDialog(
            onDismissRequest = { showNameDialog = false },
            containerColor = Color(0xFF1E1E1E),
            title = { Text("Name Your Routine", color = Color.White) },
            text = {
                OutlinedTextField(
                    value = routineName,
                    onValueChange = { routineName = it },
                    placeholder = { Text("e.g. Chest & Triceps Dominator", color = Color.Gray) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color(0xFFE0FF63),
                        unfocusedBorderColor = Color.DarkGray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (routineName.isBlank()) {
                            Toast.makeText(context, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        scope.launch {
                            isSaving = true
                            try {
                                val ids = selectedExercises.map { it.id }
                                val res = RetrofitClient.api.saveCustomRoutine(SaveCustomRoutineRequest(routineName, ids))
                                if (res.isSuccessful && res.body()?.status == true) {
                                    Toast.makeText(context, "Routine Saved!", Toast.LENGTH_SHORT).show()
                                    showNameDialog = false
                                    selectedExercises.clear()
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, res.body()?.message ?: "Failed", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(context, "Network error", Toast.LENGTH_SHORT).show()
                            }
                            isSaving = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63))
                ) {
                    if (isSaving) CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(20.dp))
                    else Text("SAVE", color = Color.Black)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNameDialog = false }) {
                    Text("CANCEL", color = Color.Gray)
                }
            }
        )
    }
}
