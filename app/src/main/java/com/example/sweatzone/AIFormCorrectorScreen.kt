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
import androidx.compose.foundation.Image
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Warning
import androidx.compose.ui.layout.ContentScale
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
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.SaveScoreRequest
import com.example.sweatzone.data.dto.ScoreItem
import com.example.sweatzone.ui.components.AppBottomNavigationBar
import com.example.sweatzone.utils.FormAnalyzer
import com.example.sweatzone.utils.FormResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.pose.PoseDetection
import com.google.mlkit.vision.pose.PoseLandmark
import com.google.mlkit.vision.pose.defaults.PoseDetectorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/* ====================== SCREEN ====================== */

@Composable
fun AIFormCorrectorScreen(navController: NavController) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    var selectedWorkout by remember { mutableStateOf("Squat") }
    var isAnalyzing by remember { mutableStateOf(false) }
    
    // State for Analysis Result
    var formResult by remember { mutableStateOf<FormResult?>(null) }
    
    // State for Top Scores
    var topScores by remember { mutableStateOf<List<ScoreItem>>(emptyList()) }
    var isLoadingScores by remember { mutableStateOf(false) }

    // Fetch Top Scores when screen loads or workout changes
    LaunchedEffect(selectedWorkout) {
        isLoadingScores = true
        try {
            val userId = com.example.sweatzone.data.local.TokenManager.getUserId()
            if (userId != -1) {
                val response = RetrofitClient.api.getTopScores(userId, selectedWorkout)
                if (response.isSuccessful) {
                    topScores = response.body()?.data ?: emptyList()
                }
            }
        } catch (e: Exception) {
            // Handle error silently or show snackbar
        } finally {
            isLoadingScores = false
        }
    }

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
        formResult = null // Reset previous result

        scope.launch(Dispatchers.IO) {
            try {
                val userId = com.example.sweatzone.data.local.TokenManager.getUserId()
                val retriever = android.media.MediaMetadataRetriever()
                retriever.setDataSource(context, uri)

                val duration =
                    retriever.extractMetadata(
                        android.media.MediaMetadataRetriever.METADATA_KEY_DURATION
                    )?.toLong() ?: 0L

                val analyzeDuration = minOf(duration, 10_000)

                var minKneeAngle = 180.0
                var maxKneeAngle = 0.0
                var minBackAngle = 180.0
                var minElbowAngle = 180.0
                var maxElbowAngle = 0.0
                var minBodyAngle = 180.0
                var validFrames = 0
                var verticalFrames = 0
                var horizontalFrames = 0

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

                            // Check body orientation
                            val isVertical = kotlin.math.abs(shoulder.position.y - ankle.position.y) >
                                    kotlin.math.abs(shoulder.position.x - ankle.position.x)
                            if (isVertical) {
                                verticalFrames++
                            } else {
                                horizontalFrames++
                            }

                            minBodyAngle = minOf(
                                minBodyAngle,
                                FormAnalyzer.calculateAngle(shoulder, hip, ankle)
                            )

                            if (knee != null) {
                                val kneeAngle = FormAnalyzer.calculateAngle(hip, knee, ankle)
                                minKneeAngle = minOf(minKneeAngle, kneeAngle)
                                maxKneeAngle = maxOf(maxKneeAngle, kneeAngle)
                                minBackAngle = minOf(
                                    minBackAngle,
                                    FormAnalyzer.calculateAngle(shoulder, hip, knee)
                                )
                            }

                            if (elbow != null && wrist != null) {
                                val elbowAngle = FormAnalyzer.calculateAngle(shoulder, elbow, wrist)
                                minElbowAngle = minOf(minElbowAngle, elbowAngle)
                                maxElbowAngle = maxOf(maxElbowAngle, elbowAngle)
                            }
                        }
                    } catch (_: Exception) {
                    } finally {
                        bitmap.recycle() // Important to prevent OOM
                    }
                }

                retriever.release()

                // Perform Analysis
                val result = when (selectedWorkout) {
                    "Squat" -> FormAnalyzer.analyzeSquat(minKneeAngle, maxKneeAngle, minBackAngle, validFrames, verticalFrames)
                    "Push-up" -> FormAnalyzer.analyzePushUp(minElbowAngle, maxElbowAngle, minBodyAngle, validFrames, horizontalFrames)
                    "Plank" -> FormAnalyzer.analyzePlank(minBodyAngle, validFrames, horizontalFrames)
                    else -> FormResult(0, emptyList(), "Unsupported workout", false, false)
                }

                // Save Score to Backend only if workout is successfully detected
                if (userId != -1 && validFrames >= 5 && result.isWorkoutDetected) {
                      try {
                        RetrofitClient.api.saveFormScore(
                            SaveScoreRequest(userId, selectedWorkout, result.score, null)
                        )
                        // Refresh top scores
                        val scoresResponse = RetrofitClient.api.getTopScores(userId, selectedWorkout)
                        if (scoresResponse.isSuccessful) {
                           withContext(Dispatchers.Main) {
                               topScores = scoresResponse.body()?.data ?: emptyList()
                           }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                withContext(Dispatchers.Main) {
                    isAnalyzing = false
                    formResult = result
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    isAnalyzing = false
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    Scaffold(
        topBar = { AIFormCorrectorTopBar(navController) },
        bottomBar = { AppBottomNavigationBar(navController) },
        containerColor = Color(0xFFFFF0F5) // Sweatzone soft pink background
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(16.dp))

            // Workout Selector
            Text("Select Exercise", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C2449))
            Spacer(Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                listOf("Squat", "Push-up", "Plank").forEach {
                    WorkoutChip(it, selectedWorkout == it) { selectedWorkout = it }
                }
            }
            
            Spacer(Modifier.height(24.dp))

            // Result Section (If available)
            formResult?.let { result ->
                FormResultSection(selectedWorkout, result)
                Spacer(Modifier.height(24.dp))
            }

            // Upload Box (Only show if not analyzing)
            if (!isAnalyzing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (formResult == null) 160.dp else 64.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White)
                        .dashedBorder(2.dp, Color(0xFF2C2449).copy(alpha = 0.4f), 20.dp)
                        .clickable { launcher.launch("video/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        if (formResult == null) {
                            Icon(
                                imageVector = Icons.Default.CloudUpload,
                                contentDescription = "Upload Video",
                                tint = Color(0xFF2C2449),
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Tap to upload video for analysis",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C2449),
                                fontSize = 15.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Supports standard video formats up to 10s",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CloudUpload,
                                    contentDescription = "Analyze New Video",
                                    tint = Color(0xFF2C2449),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    "Analyze a New Video",
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF2C2449),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            } else {
                 Box(Modifier.fillMaxWidth().height(160.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color(0xFF2C2449))
                        Spacer(Modifier.height(12.dp))
                        Text("Analyzing Pose Keypoints...", color = Color(0xFF2C2449), fontWeight = FontWeight.Medium)
                        Text("This will take a few seconds", color = Color.Gray, fontSize = 12.sp)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Show interactive setup guide card since raw video files are empty (ID = 0)
            val videoRes = when (selectedWorkout) {
                "Squat" -> 0
                "Push-up" -> 0
                "Plank" -> 0
                else -> 0
            }

            if (videoRes != 0) {
                SampleWorkoutVideo(selectedWorkout)
            } else {
                WorkoutSetupGuideCard(selectedWorkout)
            }

            Spacer(Modifier.height(32.dp))

            // Top Scores Section
            Text("Your Top Scores", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2C2449))
            Spacer(Modifier.height(12.dp))
            
            if (isLoadingScores) {
                CircularProgressIndicator(color = Color(0xFF2C2449), modifier = Modifier.size(24.dp))
            } else if (topScores.isEmpty()) {
                Text("No scores yet. Upload a video!", color = Color.Gray)
            } else {
                topScores.forEachIndexed { index, score ->
                    TopScoreItem(index + 1, score)
                }
            }
            
             Spacer(Modifier.height(24.dp))
        }
    }
}

/* ====================== VIDEO / MOCK ====================== */

@Composable
fun WorkoutSetupGuideCard(workout: String) {
    val imageRes = when (workout) {
        "Squat" -> R.drawable.legimg
        "Push-up" -> R.drawable.chestimg
        "Plank" -> R.drawable.absimg
        else -> R.drawable.chestimg
    }

    val instructions = when (workout) {
        "Squat" -> listOf(
            "Place camera at hip level, 6-8 feet away.",
            "Position your full body in side-profile view.",
            "Ensure you stand vertically with full clearance."
        )
        "Push-up" -> listOf(
            "Place camera at floor level, 6 feet away.",
            "Capture your body horizontally from the side profile.",
            "Ensure head, hips, and heels are in full view."
        )
        "Plank" -> listOf(
            "Position camera parallel to the floor, 5-7 feet away.",
            "Capture your body horizontally from head to toe.",
            "Maintain straight alignment in a static hold."
        )
        else -> emptyList()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Image(
                    painter = androidx.compose.ui.res.painterResource(id = imageRes),
                    contentDescription = "$workout Setup Guide",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Camera Setup for Perfect $workout",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Follow these positioning tips for accurate AI results",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
            }

            Column(modifier = Modifier.padding(16.dp)) {
                instructions.forEachIndexed { index, tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF2C2449)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${index + 1}",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = tip,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun SampleWorkoutVideo(workout: String) {

    val context = LocalContext.current

    val videoRes = when (workout) {
        "Squat" -> 0
        "Push-up" -> 0
        "Plank" -> 0
        else -> 0
    }

    val player = remember(videoRes) {
        ExoPlayer.Builder(context).build().apply {
            val uri = androidx.media3.datasource.RawResourceDataSource.buildRawResourceUri(videoRes)
            setMediaItem(MediaItem.fromUri(uri))
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
            playWhenReady = true
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
                PlayerView(ctx).apply { 
                    useController = true
                    resizeMode = androidx.media3.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                }
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
            Text("Perfect $workout Video Guide", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Watch correct biomechanics", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

/* ====================== UI COMPONENTS ====================== */

@Composable
fun FormResultSection(workoutName: String, result: FormResult) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (!result.isWorkoutDetected) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)), // Soft red background
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            text = "Workout Not Detected",
                            color = Color(0xFFC62828),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    val feedbackMsg = result.feedback.firstOrNull()?.message ?: "We couldn't detect the selected workout."
                    Text(
                        text = feedbackMsg,
                        color = Color(0xFFD32F2F),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = "Tip: ${result.quickTip}",
                        color = Color(0xFF333333),
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "$workoutName Form Result",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2449)
                    )
                    Spacer(Modifier.height(16.dp))

                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(120.dp)
                    ) {
                        CircularProgressIndicator(
                            progress = { result.score / 100f },
                            modifier = Modifier.fillMaxSize(),
                            color = if (result.score >= 80) Color(0xFF43A047) else Color(0xFFFFB300),
                            strokeWidth = 10.dp,
                            trackColor = Color(0xFFEEEEEE)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "${result.score}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2C2449)
                            )
                            Text(
                                text = "/ 100",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    
                    Spacer(Modifier.height(12.dp))
                    
                    Text(
                        text = if (result.score >= 80) "Excellent Form!" else "Needs Adjustment",
                        color = if (result.score >= 80) Color(0xFF43A047) else Color(0xFFFFB300),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(Modifier.height(20.dp))

            // Feedback Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Form Feedback Detail",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C2449)
                    )
                    Spacer(Modifier.height(16.dp))

                    result.feedback.forEachIndexed { index, item ->
                        FeedbackRow(item)
                        if (index < result.feedback.lastIndex) {
                            Divider(color = Color(0xFFF5F5F5), thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Pro Tip Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)), // Soft blue
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Tip",
                        tint = Color(0xFF1976D2),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column {
                        Text("Pro Tip", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(Modifier.height(4.dp))
                        Text(result.quickTip, color = Color(0xFF0D47A1), fontSize = 13.sp, lineHeight = 18.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackRow(item: com.example.sweatzone.utils.FeedbackItem) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (item.isPositive) Color(0xFFE8F5E9) else Color(0xFFFFEBEE)), // Green vs Red bg
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (item.isPositive) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (item.isPositive) Color(0xFF43A047) else Color(0xFFD32F2F),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(
            text = item.message,
            fontSize = 15.sp,
            color = Color(0xFF1A1C1E),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun TopScoreItem(rank: Int, score: ScoreItem) {
    val badgeColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> Color(0xFF2C2449) // Theme Purple
    }
    val badgeTextColor = if (rank <= 2) Color.Black else Color.White

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(badgeColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#$rank",
                    color = badgeTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Score: ${score.score} / 100", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF2C2449))
                Text(score.createdAt.take(10), color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun WorkoutChip(name: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(name, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF2C2449),
            selectedLabelColor = Color.White,
            containerColor = Color.White.copy(alpha = 0.8f),
            labelColor = Color(0xFF2C2449)
        ),
        shape = RoundedCornerShape(12.dp),
        border = null
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIFormCorrectorTopBar(navController: NavController) {
    CenterAlignedTopAppBar(
        title = { Text("AI Form Corrector", fontWeight = FontWeight.Bold, color = Color(0xFF2C2449)) },
        navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF2C2449)
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color(0xFFFFF0F5)
        )
    )
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

