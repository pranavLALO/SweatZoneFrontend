package com.example.sweatzone

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayCircleFilled
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar

@Composable
fun BeginnerArmsWorkoutsScreen(navController: NavController) {

    // Elegant pale pink background matching your reference image
    val pinkBg = Color(0xFFFFF0F5)

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
                        painter = painterResource(id = R.drawable.armsimg), // Ensure you have this image
                        contentDescription = "Arms Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Arms",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            // --- Exercise 1: Dumbbell Bicep Curls ---
            item {
                ArmsExerciseItem(
                    title = "Dumbbell Bicep Curls",
                    // Ensure 'bicep_curls_video.mp4' is in res/raw
                    videoResId = R.raw.bicep_curls_video,
                    instructions = listOf(
                        "Stand upright with a dumbbell in each hand, arms fully extended, and palms facing forward.",
                        "Keep your elbows close to your torso at all times.",
                        "Exhale and curl the weights while contracting your biceps.",
                        "Continue to raise the weights until your biceps are fully contracted and the dumbbells are at shoulder level.",
                        "Hold the contracted position for a brief pause as you squeeze your biceps.",
                        "Inhale and slowly begin to lower the dumbbells back to the starting position."
                    ),
                    benefits = listOf(
                        "Isolates the biceps brachii for maximum muscle growth and definition.",
                        "Improves arm strength and grip strength.",
                        "Allows for a full range of motion compared to barbells.",
                        "Corrects strength imbalances between the left and right arm."
                    )
                )
            }

            // --- Exercise 2: Hammer Curls ---
            item {
                ArmsExerciseItem(
                    title = "Hammer Curls",
                    // Ensure 'hammer_curls_video.mp4' is in res/raw
                    videoResId = R.raw.hammer_curls_video,
                    instructions = listOf(
                        "Stand upright with a dumbbell in each hand, palms facing your torso (neutral grip).",
                        "Keep your elbows close to your torso at all times.",
                        "Exhale and curl the weights forward while contracting the biceps. Only your forearms should move.",
                        "Continue the movement until your biceps are fully contracted and the dumbbells are at shoulder level.",
                        "Pause for a second at the top, then slowly lower the dumbbells back to the starting position as you inhale."
                    ),
                    benefits = listOf(
                        "Targets the brachialis and brachioradialis in addition to the biceps.",
                        "Helps build thicker, wider-looking arms.",
                        "Improves grip strength due to the neutral wrist position.",
                        "Reduces stress on the wrists compared to standard bicep curls."
                    )
                )
            }

            // --- Exercise 3: Tricep Rope Pushdown ---
            item {
                ArmsExerciseItem(
                    title = "Tricep Rope Pushdown",
                    // Ensure 'rope_pushdown_video.mp4' is in res/raw
                    videoResId = R.raw.rope_pushdown_video,
                    instructions = listOf(
                        "Attach a double-ended rope to the high pulley of a cable machine.",
                        "Grab the rope ends with a neutral grip, palms facing each other.",
                        "Stand upright with a slight forward lean, keeping elbows tucked firmly by your sides.",
                        "Extend your elbows to push the rope down, spreading the handles apart at the bottom for a full contraction.",
                        "Slowly return to the starting position, keeping elbows stationary throughout the movement."
                    ),
                    benefits = listOf(
                        "Isolates the triceps effectively, emphasizing the lateral head.",
                        "Allows for a longer range of motion compared to a straight bar.",
                        "Constant tension from the cable maximizes muscle engagement.",
                        "Improves lockout strength for pressing movements."
                    )
                )
            }

            // --- Exercise 4: Overhead Dumbbell Tricep Extension ---
            item {
                ArmsExerciseItem(
                    title = "Overhead Dumbbell Tricep Extension",
                    // Ensure 'tricep_extension_video.mp4' is in res/raw
                    videoResId = R.raw.tricep_extension_video,
                    instructions = listOf(
                        "Stand or sit upright holding a dumbbell with both hands.",
                        "Lift the dumbbell over your head until both arms are fully extended.",
                        "Keeping your upper arms close to your head and elbows in, slowly lower the dumbbell behind your head.",
                        "Lower the weight until your forearms touch your biceps.",
                        "Extend your arms back up to the starting position, squeezing your triceps at the top."
                    ),
                    benefits = listOf(
                        "Isolates the triceps, specifically the long head.",
                        "Improves arm strength and stability.",
                        "Can be performed seated or standing to vary core engagement.",
                        "Helps in achieving balanced arm development."
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

// --- REUSABLE COMPONENT DEFINITION ---
@OptIn(UnstableApi::class)
@Composable
fun ArmsExerciseItem(
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
