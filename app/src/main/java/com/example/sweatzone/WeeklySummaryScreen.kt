package com.example.sweatzone

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
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
import com.example.sweatzone.data.api.RetrofitClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeeklySummaryScreen(
    navController: NavController,
    userViewModel: UserViewModel = viewModel()
) {
    var summary by remember { mutableStateOf<com.example.sweatzone.data.dto.WeeklySummaryResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val response = RetrofitClient.api.getWeeklySummary(userViewModel.userId)
                if (response.isSuccessful && response.body()?.status == true) {
                    summary = response.body()
                }
            } catch (e: Exception) {
                // handle error
            } finally {
                isLoading = false
            }
        }
    }

    val premiumDark = Color(0xFF1A1A1A)
    val premiumAccent = Color(0xFFE0FF63)
    val cardBg = Color(0xFF2C2C2C)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("7-Day Performance", fontWeight = FontWeight.Bold, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = premiumDark)
            )
        },
        containerColor = premiumDark
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = premiumAccent)
            }
        } else if (summary != null) {
            val data = summary!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Workouts Section
                Text(
                    text = "WORKOUTS",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "SESSIONS",
                        value = "${data.total_workouts}",
                        icon = Icons.Default.FitnessCenter,
                        accentColor = premiumAccent
                    )
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "VOLUME",
                        value = "${data.total_volume} kg",
                        icon = Icons.Default.MonitorWeight,
                        accentColor = Color(0xFFFF5722)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                SummaryStatCard(
                    modifier = Modifier.fillMaxWidth(),
                    label = "TOTAL DURATION",
                    value = formatDuration(data.total_time),
                    icon = Icons.Default.Timer,
                    accentColor = Color(0xFF00E5FF)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Diet Section
                Text(
                    text = "NUTRITION & HYDRATION",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "AVG CALORIES",
                        value = "${data.avg_calories} kcal",
                        icon = Icons.Default.Restaurant,
                        accentColor = Color(0xFFFFC107)
                    )
                    SummaryStatCard(
                        modifier = Modifier.weight(1f),
                        label = "WATER INTAKE",
                        value = "${data.total_water} gl",
                        icon = Icons.Default.WaterDrop,
                        accentColor = Color(0xFF2196F3)
                    )
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Failed to load summary", color = Color.Red)
            }
        }
    }
}
