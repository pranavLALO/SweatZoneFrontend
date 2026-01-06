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
fun BeginnerLegsWorkoutsScreen(navController: NavController) {

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
            item {
                Box(modifier = Modifier.fillMaxWidth().height(150.dp)) {
                    Image(
                        painter = painterResource(id = R.drawable.legimg),
                        contentDescription = "Legs Workout",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Legs",
                            color = Color.White,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    }
                }
            }

            item {
                LegsExerciseItem(
                    title = "Bodyweight Squats",
                    videoResId = R.raw.squats_video,
                    instructions = listOf(
                        "Stand with feet shoulder-width apart, toes pointing slightly outwards.",
                        "Keep your chest up and core engaged. Lower your hips back and down as if sitting in a chair.",
                        "Ensure your knees do not collapse inward; keep them aligned with your toes.",
                        "Pause for a second at the bottom of the squat.",
                        "Push through your heels to return to the standing position, squeezing your glutes at the top."
                    ),
                    benefits = listOf(
                        "Strengthens your quads, hamstrings, and glutes.",
                        "Improves core strength and stability.",
                        "Increases flexibility in your hips and ankles."
                    )
                )
            }

            item {
                LegsExerciseItem(
                    title = "Leg Press",
                    videoResId = R.raw.leg_press_video,
                    instructions = listOf(
                        "Sit on the leg press machine with your back flat against the padded support.",
                        "Place your feet on the platform shoulder-width apart.",
                        "Push the platform away from you by extending your legs, but do not lock your knees.",
                        "Slowly lower the platform back towards your chest until your legs are bent at a 90-degree angle.",
                        "Push the platform back to the starting position."
                    ),
                    benefits = listOf(
                        "Allows you to lift heavy loads safely with back support.",
                        "Isolates the quadriceps effectively.",
                        "Good alternative to squats for those with back issues."
                    )
                )
            }

            item {
                LegsExerciseItem(
                    title = "Walking Lunges",
                    videoResId = R.raw.walking_lunges_video,
                    instructions = listOf(
                        "Stand tall with feet together, core engaged.",
                        "Step forward with your right leg, lowering your hips until both knees are bent at a 90-degree angle.",
                        "Ensure your front knee is directly above your ankle and your back knee is hovering just above the ground.",
                        "Push off your back foot to bring it forward to meet your right foot, returning to standing position.",
                        "Repeat the movement on the other leg, walking forward."
                    ),
                    benefits = listOf(
                        "Great for balance, stability and coordination.",
                        "Builds functional leg strength.",
                        "Requires significant core stabilization."
                    )
                )
            }

            item {
                LegsExerciseItem(
                    title = "Leg Extension",
                    videoResId = R.raw.leg_extension_video,
                    instructions = listOf(
                        "Sit on the leg extension machine and adjust the pad to rest on your lower shins.",
                        "Select an appropriate weight.",
                        "Extend your legs until they are straight, squeezing your quadriceps at the top.",
                        "Hold for a second, then slowly lower the weight back to the starting position.",
                        "Avoid using momentum."
                    ),
                    benefits = listOf(
                        "Isolates the quadriceps muscles effectively.",
                        "Helps to strengthen the knee joint.",
                        "Easy to learn and perform for beginners."
                    )
                )
            }

            item {
                LegsExerciseItem(
                    title = "Hamstring Curl",
                    videoResId = R.raw.hamstring_curl_video,
                    instructions = listOf(
                        "Adjust the machine lever to fit your height and lie face down on the bench.",
                        "Place the back of your lower legs against the padded lever (just below calves).",
                        "Squeeze your hamstrings to curl the weight up towards your glutes as far as possible.",
                        "Hold briefly at the top.",
                        "Slowly lower the weight back to the starting position."
                    ),
                    benefits = listOf(
                        "Directly targets and strengthens the hamstring muscles.",
                        "Improves knee stability and health.",
                        "Crucial for balancing quadricep development to prevent injury."
                    )
                )
            }

            item {
                LegsExerciseItem(
                    title = "Standing Calf Raises",
                    videoResId = R.raw.calf_raises_video,
                    instructions = listOf(
                        "Stand with the balls of your feet on the edge of a step or platform. Let your heels hang off.",
                        "Slowly raise your heels as high as possible, pushing through the balls of your feet.",
                        "Pause at the top for a full contraction.",
                        "Slowly lower your heels down until you feel a stretch in your calves.",
                        "Repeat."
                    ),
                    benefits = listOf(
                        "Strengthens the gastrocnemius and soleus muscles in your calves.",
                        "Improves ankle stability and strength.",
                        "Enhances running and jumping performance."
                    )
                )
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun LegsExerciseItem(
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
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                                repeatMode = Player.REPEAT_MODE_ONE
                                playWhenReady = true
                                prepare()
                                volume = 0f
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