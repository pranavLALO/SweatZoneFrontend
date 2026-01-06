package com.example.sweatzone.ui.components

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.sweatzone.R
import com.example.sweatzone.Screen

// --- ADDED BACK MISSING COMPOSABLES ---

@Composable
fun TopBarSection() {
    val darkBg = Color(0xFF2C2449)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(darkBg)
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hi",
                color = Color(0xFFB39DDB),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "It's time to challenge your limits.",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile",
            tint = Color(0xFFB39DDB),
            modifier = Modifier.size(32.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeCard(
    title: String,
    containerColor: Color,
    textColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = Modifier.aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Icon(
                imageVector = Icons.Default.FitnessCenter,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.fillMaxSize().padding(16.dp).clip(RoundedCornerShape(24.dp))
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                            startY = 100f
                        )
                    )
            )

            Text(
                text = title,
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun BlogPostCard() { // Simplified to not require parameters for now
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(painter = painterResource(id = R.drawable.chestimg), contentDescription = null, contentScale = ContentScale.Crop, modifier = Modifier.fillMaxSize())
            Box(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)), startY = 100f)))
            Text(
                text = "Mastering the Basics: A Guide to\nPerfect Push-Ups",
                color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.BottomStart).padding(20.dp)
            )
        }
    }
}

// --- END OF ADDED COMPOSABLES ---


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutCategoryCard(
    title: String,
    imageRes: Int,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun ExerciseItem(
    title: String,
    videoResId: Int,
    instructions: List<String>,
    benefits: List<String>
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(12.dp))

            // --- Embedded Video Player ---
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
                            if (videoResId != 0) { // Check for a valid resource ID
                                val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
                                val mediaItem = MediaItem.fromUri(videoUri)
                                setMediaItem(mediaItem)
                                repeatMode = Player.REPEAT_MODE_ONE // Loop the video
                                playWhenReady = true
                                prepare()
                                volume = 0f // Start muted for autoplay UX
                            } else {
                                // Handle invalid videoResId, maybe show a placeholder or log an error
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
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = true
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Instructions ---
            Text("Instructions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            instructions.forEachIndexed { index, instruction ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("${index + 1}. ", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
                    Text(instruction, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- Benefits ---
            Text("Benefits", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            benefits.forEach { benefit ->
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("• ", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.DarkGray)
                    Text(benefit, fontSize = 14.sp, color = Color.DarkGray, lineHeight = 20.sp)
                }
            }
        }
    }
}