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
fun UpperBodyHomeWorkoutScreen(navController: NavController) {
    val userViewModel: UserViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    // Elegant pale pink background to match the theme
    val pinkBg = Color(0xFFFFF0F5)
    val startTime = androidx.compose.runtime.remember { System.currentTimeMillis() }

    Scaffold(
        bottomBar = {
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
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.chestimg), // Using chest image as upper body placeholder
                        contentDescription = "Home Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Column(modifier = Modifier.padding(start = 16.dp)) {
                            Text(
                                text = "Home",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Workout",
                                color = Color.White,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // --- Exercise 1: Push-Ups ---
            item {
                UpperBodyExerciseItem(
                    title = "Push-Ups",
                    videoResId = 0, // Ensure you have pushup_video.mp4
                    instructions = listOf(
                        "Stand with your feet shoulder-width apart, chest up, and core engaged.",
                        "Lower your hips back and down as if sitting in a chair, keeping your back straight.",
                        "Lower as far as you comfortably can, aiming for your thighs to be parallel to the floor.",
                        "Push through your heels to return to the starting position, squeezing your glutes at the top.",
                        "Repeat for the desired number of repetitions."
                    ),
                    benefits = listOf(
                        "Builds strength in glutes, quads, and hamstrings.",
                        "Improves core stability and balance.",
                        "Enhances mobility in hips and ankles.",
                        "A fundamental functional movement for daily life."
                    )
                )
            }

            // --- Exercise 2: Incline Push-Ups ---
            item {
                UpperBodyExerciseItem(
                    title = "Incline Push-Ups",
                    videoResId = 0, // Ensure you have incline_pushup_video.mp4
                    instructions = listOf(
                        "Place your hands on an elevated surface like a bench, step, or sturdy chair, slightly wider than shoulder-width apart.",
                        "Step your feet back so your body forms a straight line from head to heels; engage your core and glutes.",
                        "Lower your chest toward the edge of the surface by bending your elbows, keeping them tucked at a 45-degree angle.",
                        "Push through your palms to extend your arms and return to the starting position. Repeat for the desired reps."
                    ),
                    benefits = listOf(
                        "Targets the chest (pectorals) and triceps effectively.",
                        "Great for beginners pushing up, making it easier than floor push-ups.",
                        "Engages the core for stability and improves posture.",
                        "Reduces stress on the shoulders compared to floor push-ups."
                    )
                )
            }

            // --- Exercise 3: Pike Push-Ups ---
            item {
                UpperBodyExerciseItem(
                    title = "Pike Push-Ups",
                    videoResId = 0, // Ensure you have pike_pushup_video.mp4
                    instructions = listOf(
                        "Start in a downward dog yoga position with your hands shoulder-width apart. Your body should form an inverted V shape.",
                        "Keep your legs straight and heels elevated if necessary. Your weight should be distributed between your hands and feet.",
                        "Bend your elbows to lower your head towards the ground, aiming for a spot slightly in front of your hands to form a tripod shape.",
                        "Push back up through your shoulders to the starting position, fully extending your arms while keeping the hips high."
                    ),
                    benefits = listOf(
                        "Excellent for building shoulder strength and mass (anterior deltoids).",
                        "Strengthens triceps and upper chest muscles.",
                        "Improves core stability and overhead pushing mechanics.",
                        "Acts as a progression exercise towards a full handstand push-up."
                    )
                )
            }

            // --- Exercise 4: Push Up (Weighted) ---
            item {
                UpperBodyExerciseItem(
                    title = "Push Up (Weighted)",
                    videoResId = 0, // Ensure you have weighted_pushup_video.mp4
                    instructions = listOf(
                        "Assume a standard push-up position with hands slightly wider than shoulder-width. Have a partner place a weight plate on your upper back or wear a weighted vest.",
                        "Keep your core tight and body in a straight line from head to heels, avoid sagging hips or arching your back.",
                        "Lower your body by bending your elbows until your chest nearly touches the floor. Inhale as you lower down.",
                        "Push back up to the starting position explosively, exhaling as you extend fully. Repeat for the set reps."
                    ),
                    benefits = listOf(
                        "Promotes muscle growth and increases strength beyond bodyweight limits.",
                        "Enhances core stability due to the added load on the torso.",
                        "Provides greater muscle hypertrophy for the upper body.",
                        "Allows for progressive overload without increasing volume excessively."
                    )
                )
            }

            item {
                Button(
                    onClick = {
                        val duration = ((System.currentTimeMillis() - startTime) / 1000).toInt()
                        userViewModel.logWorkout("upper_body", "medium", duration) {
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

// --- REUSABLE COMPONENT DEFINITION ---
@OptIn(UnstableApi::class)
@Composable
fun UpperBodyExerciseItem(
    title: String,
    videoResId: Int,
    instructions: List<String>,
    benefits: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Title
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Embedded Video Player
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                val context = LocalContext.current
                val exoPlayer = remember {
                    ExoPlayer.Builder(context).build().apply {
                        try {
                            if (videoResId != 0) {
                                val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                                val mediaItem = MediaItem.fromUri(videoUri)
                                setMediaItem(mediaItem)
                                repeatMode = Player.REPEAT_MODE_ONE // Loops the video
                                playWhenReady = true
                                prepare()
                                volume = 0f // Start muted
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

                DisposableEffect(Unit) {
                    onDispose {
                        exoPlayer.release()
                    }
                }

                AndroidView(
                    factory = {
                        PlayerView(context).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Instructions Section
            Text(
                text = "Instructions",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            instructions.forEachIndexed { index, instruction ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "${index + 1}. ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = instruction,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Benefits Section
            Text(
                text = "Benefits",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            benefits.forEach { benefit ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = "• ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                    Text(
                        text = benefit,
                        fontSize = 14.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun UpperBodyHomeWorkoutScreenPreview() {
    SweatzoneTheme {
        UpperBodyHomeWorkoutScreen(navController = rememberNavController())
    }
}
