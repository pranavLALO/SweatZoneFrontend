package com.example.sweatzone

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.math.abs
import kotlin.math.atan2

/* ====================== SCREEN ====================== */

@Composable
fun AIFormCorrectorScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var selectedWorkout by remember { mutableStateOf("Squat") }
    var isAnalyzing by remember { mutableStateOf(false) }
    var resultMessage by remember { mutableStateOf("") }

    val poseDetector = remember {
        PoseDetection.getClient(
            PoseDetectorOptions.Builder()
                .setDetectorMode(PoseDetectorOptions.SINGLE_IMAGE_MODE)
                .build()
        )
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->

        if (uri == null) return@rememberLauncherForActivityResult

        isAnalyzing = true
        resultMessage = "Analyzing $selectedWorkout form..."

        scope.launch(Dispatchers.IO) {
            try {
                val retriever = android.media.MediaMetadataRetriever()
                retriever.setDataSource(context, uri)

                val duration =
                    retriever.extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                    )?.toLong() ?: 0L

                val analyzeDuration = minOf(duration, 10_000)

                var minKneeAngle = 180.0
                var minBackAngle = 180.0
                var minElbowAngle = 180.0
                var minBodyAngle = 180.0
                var validFrames = 0

                for (time in 0 until analyzeDuration step 400) {
                    val bitmap = retriever.getFrameAtTime(
                        time * 1000,
                        android.media.MediaMetadataRetriever.OPTION_CLOSEST_SYNC
                    ) ?: continue

                    val image = InputImage.fromBitmap(bitmap, 0)

                    try {
                        val pose = poseDetector.process(image).await()

                        val shoulder = pose.getPoseLandmark(PoseLandmark.LEFT_SHOULDER)
                        val elbow = pose.getPoseLandmark(PoseLandmark.LEFT_ELBOW)
                        val wrist = pose.getPoseLandmark(PoseLandmark.LEFT_WRIST)
                        val hip = pose.getPoseLandmark(PoseLandmark.LEFT_HIP)
                        val knee = pose.getPoseLandmark(PoseLandmark.LEFT_KNEE)
                        val ankle = pose.getPoseLandmark(PoseLandmark.LEFT_ANKLE)

                        if (shoulder != null && hip != null && ankle != null) {
                            validFrames++

                            minBodyAngle = minOf(
                                minBodyAngle,
                                calculateAngle(shoulder, hip, ankle)
                            )

                            if (knee != null) {
                                minKneeAngle = minOf(
                                    minKneeAngle,
                                    calculateAngle(hip, knee, ankle)
                                )
                                minBackAngle = minOf(
                                    minBackAngle,
                                    calculateAngle(shoulder, hip, knee)
                                )
                            }

                            if (elbow != null && wrist != null) {
                                minElbowAngle = minOf(
                                    minElbowAngle,
                                    calculateAngle(shoulder, elbow, wrist)
                                )
                            }
                        }
                    } catch (_: Exception) {
                    } finally {
                        bitmap.recycle()
                    }
                }

                retriever.release()

                val finalMessage = when (selectedWorkout) {
                    "Squat" -> analyzeSquat(minKneeAngle, minBackAngle, validFrames)
                    "Push-up" -> analyzePushUp(minElbowAngle, minBodyAngle, validFrames)
                    "Plank" -> analyzePlank(minBodyAngle, validFrames)
                    else -> "Workout not supported"
                }

                withContext(Dispatchers.Main) {
                    isAnalyzing = false
                    resultMessage = finalMessage
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isAnalyzing = false
                    Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = { AIFormCorrectorTopBar(navController) },
        bottomBar = { AppBottomNavigationBar(navController, homeRoute = "tools") },
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(scrollState)
        ) {

            Text("Select Workout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            WorkoutSelectionItem("Squat", selectedWorkout == "Squat") {
                selectedWorkout = "Squat"
            }
            WorkoutSelectionItem("Push-up", selectedWorkout == "Push-up") {
                selectedWorkout = "Push-up"
            }
            WorkoutSelectionItem("Plank", selectedWorkout == "Plank") {
                selectedWorkout = "Plank"
            }

            Spacer(Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .dashedBorder(2.dp, Color.LightGray, 16.dp)
                    .clickable { launcher.launch("video/*") },
                contentAlignment = Alignment.Center
            ) {
                if (isAnalyzing) {
                    CircularProgressIndicator()
                } else {
                    Text("Tap to upload video")
                }
            }

            Spacer(Modifier.height(24.dp))

            SampleWorkoutVideo(selectedWorkout)

            Spacer(Modifier.height(24.dp))

            if (resultMessage.isNotEmpty()) {
                Text(
                    text = resultMessage,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = if (resultMessage.contains("✅")) Color(0xFF2E7D32) else Color.Red
                )
            }
        }
    }
}

/* ====================== ANALYSIS ====================== */

fun analyzeSquat(knee: Double, back: Double, frames: Int) = when {
    frames < 5 -> "❌ Body not detected"
    knee > 120 -> "⚠️ Squat too shallow"
    back < 90 -> "⚠️ Back bending too much"
    else -> "✅ Good squat form!"
}

fun analyzePushUp(elbow: Double, body: Double, frames: Int) = when {
    frames < 5 -> "❌ Body not detected"
    elbow > 100 -> "⚠️ Push-up too shallow"
    body < 160 -> "⚠️ Hips sagging"
    else -> "✅ Good push-up form!"
}

fun analyzePlank(body: Double, frames: Int) = when {
    frames < 5 -> "❌ Body not detected"
    body < 165 -> "⚠️ Body not straight"
    else -> "✅ Solid plank posture!"
}

/* ====================== VIDEO ====================== */

@OptIn(UnstableApi::class)
@Composable
fun SampleWorkoutVideo(workout: String) {

    val context = LocalContext.current

    val videoRes = when (workout) {
        "Squat" -> R.raw.squats_video
        "Push-up" -> R.raw.pushup_video
        "Plank" -> R.raw.plank_video
        else -> R.raw.squats_video
    }

    val uri = Uri.parse("android.resource://${context.packageName}/$videoRes")

    val player = remember(videoRes) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
        }
    }

    DisposableEffect(videoRes) {
        onDispose { player.release() }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.Black)
    ) {

        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply { useController = true }
            },
            update = { it.player = player },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text("Perfect $workout Form", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Watch and learn", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

/* ====================== UI HELPERS ====================== */

fun calculateAngle(a: PoseLandmark, b: PoseLandmark, c: PoseLandmark): Double {
    val angle = Math.toDegrees(
        atan2((c.position.y - b.position.y).toDouble(), (c.position.x - b.position.x).toDouble()) -
                atan2((a.position.y - b.position.y).toDouble(), (a.position.x - b.position.x).toDouble())
    )
    val result = abs(angle)
    return if (result > 180) 360 - result else result
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIFormCorrectorTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text("AI Form Corrector") },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
            }
        }
    )
}

@Composable
fun WorkoutSelectionItem(title: String, selected: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (selected) Color(0xFFE3F2FD) else Color.White
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, modifier = Modifier.weight(1f))
            Icon(
                if (selected) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                null,
                tint = if (selected) Color(0xFF1976D2) else Color.Gray
            )
        }
    }
}

fun Modifier.dashedBorder(width: Dp, color: Color, radius: Dp) = drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
        ),
        cornerRadius = CornerRadius(radius.toPx())
    )
}
