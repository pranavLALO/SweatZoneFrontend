package com.example.sweatzone

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.sweatzone.ui.theme.SweatzoneTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodybuilderSplitsScreen(navController: NavController) {
    val bgColor = Color(0xFF1C1C1E)
    val cardColor = Color(0xFF2C2C2E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Legendary Bodybuilders", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = bgColor)
            )
        },
        bottomBar = {
            AppBottomNavigationBar(navController = navController)
        },
        containerColor = bgColor
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                BodybuilderCard(
                    name = "Arnold Schwarzenegger",
                    description = "7x Mr. Olympia, Golden Era Icon",
                    imageRes = R.drawable.arnold,
                    onClick = { navController.navigate(Screen.ArnoldWorkout.route) } // Connected navigation
                )
            }
            item {
                BodybuilderCard(
                    name = "Chris Bumstead",
                    description = "5x Mr. Olympia Classic Physique",
                    imageRes = R.drawable.cbum, // Ensure you have cbum.jpg
                    onClick = {
                        // Correctly navigate to the new screen
                        navController.navigate(Screen.CbumWorkout.route)
                    }
                )
            }

            item {
            BodybuilderCard(
                name = "Ronnie Coleman",
                description = "8x Mr. Olympia, 'The King'",
                imageRes = R.drawable.ronnie, // Ensure you have ronnie.jpg/png in res/drawable
                onClick = {   navController.navigate(Screen.RonnieWorkout.route) }
            )
        } // <-- The closing brace was moved here

            // --- ADDED STEVE REEVES & JAY CUTLER ---
            item {
                BodybuilderCard(
                    name = "Steve Reeves",
                    description = "Mr. America, Mr. World, Mr. Universe",
                    imageRes = R.drawable.steve_reeves, // Ensure image exists
                    onClick = {
                        // Correctly navigate to the new screen
                        navController.navigate(Screen.SteveReevesWorkout.route)
                    }
                )
            }

            item {
                BodybuilderCard(
                    name = "Jay Cutler",
                    description = "4x Mr. Olympia, Quad Stomp Icon",
                    imageRes = R.drawable.jay_cutler, // Ensure image exists
                    onClick = {
                        // Correctly navigate to the new screen
                        navController.navigate(Screen.JayCutlerWorkout.route)
                    }
                )
            }

            // Add more bodybuilders here
        }
    }
}

@Composable
fun BodybuilderCard(name: String, description: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 400f
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
            ) {
                Text(
                    text = name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = description,
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 16.sp
                )
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "View split",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .size(28.dp)
            )
        }
    }
}

@Preview
@Composable
fun BodybuilderSplitsScreenPreview() {
    SweatzoneTheme {
        BodybuilderSplitsScreen(rememberNavController())
    }
}
