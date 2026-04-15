package com.example.sweatzone

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun FullBodyHomeWorkoutsScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    // Elegant pale pink background to match the theme
    val pinkBg = Color(0xFFFFF0F5)
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }

    Scaffold(
        bottomBar = {
            // Assuming the homeRoute for this flow is the Beginner Home Screen
            AppBottomNavigationBar(navController = navController)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(pinkBg)
                .padding(innerPadding)
        ) {
            // --- Header Image ---
            item {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.fullbodyhiit), // Add a specific image for this
                        contentDescription = "Full Body HIIT",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Full Body HIIT",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }

            // --- Warm-up Section ---
            item {
                WorkoutSectionHeader("Warm-up (5 mins)")
            }
            item {
                HomeExerciseItem(
                    title = "Jumping Jacks",
                    details = "Duration: 1 min",
                    videoResId = 0 // Create in res/raw
                )
            }
            item {
                HomeExerciseItem(
                    title = "High Knees",
                    details = "Duration: 1 min",
                    videoResId = 0 // Create in res/raw
                )
            }


            // --- Main Workout Section ---
            item {
                WorkoutSectionHeader("Main Workout (30 mins)")
            }
            item {
                HomeExerciseItem(
                    title = "Push-ups",
                    details = "3 sets of 10-15 reps",
                    videoResId = 0
                )
            }
            item {
                HomeExerciseItem(
                    title = "Squats",
                    details = "3 sets of 15-20 reps",
                    videoResId = 0
                )
            }
            item {
                HomeExerciseItem(
                    title = "Plank",
                    details = "3 sets, hold for 30-60 secs",
                    videoResId = 0
                )
            }
            item {
                HomeExerciseItem(
                    title = "Lunges",
                    details = "3 sets of 10-12 reps per leg",
                    videoResId = 0
                )
            }
            item {
                HomeExerciseItem(
                    title = "Burpees",
                    details = "3 sets of 8-10 reps",
                    videoResId = 0 // Create in res/raw
                )
            }

            // --- Cool-down Section ---
            item {
                WorkoutSectionHeader("Cool-down (10 mins)")
            }
            item {
                HomeExerciseItem(
                    title = "Hamstring Stretch",
                    details = "Hold for 30 secs per leg",
                    videoResId = 0 // Create in res/raw
                )
            }
            item {
                HomeExerciseItem(
                    title = "Chest Stretch",
                    details = "Hold for 30 secs",
                    videoResId = 0 // Create in res/raw
                )
            }


            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("full_body", "high", duration) {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63))
                ) {
                    Text(text = "Finish Workout", color = Color.Black)
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// --- REUSABLE COMPONENTS FOR THIS SCREEN ---

@Composable
private fun WorkoutSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

@OptIn(UnstableApi::class)
@Composable
private fun HomeExerciseItem(
    title: String,
    details: String,
    videoResId: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Video Player Preview
            Box(
                modifier = Modifier
                    .size(120.dp, 80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        if (videoResId != 0) {
                            val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                            setMediaItem(MediaItem.fromUri(videoUri))
                            repeatMode = Player.REPEAT_MODE_ONE
                            playWhenReady = true
                            prepare()
                            volume = 0f // Muted by default
                        }
                    }
                }

                DisposableEffect(Unit) {
                    onDispose { exoPlayer.release() }
                }

                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = false // No controls for small preview
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Text Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = details,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FullBodyHomeWorkoutsScreenPreview() {
    SweatzoneTheme {
        FullBodyHomeWorkoutsScreen(navController = rememberNavController())
    }
}
