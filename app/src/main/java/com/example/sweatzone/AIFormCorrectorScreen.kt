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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonUnchecked
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
                                FormAnalyzer.calculateAngle(shoulder, hip, ankle)
                            )

                            if (knee != null) {
                                minKneeAngle = minOf(
                                    minKneeAngle,
                                    FormAnalyzer.calculateAngle(hip, knee, ankle)
                                )
                                minBackAngle = minOf(
                                    minBackAngle,
                                    FormAnalyzer.calculateAngle(shoulder, hip, knee)
                                )
                            }

                            if (elbow != null && wrist != null) {
                                minElbowAngle = minOf(
                                    minElbowAngle,
                                    FormAnalyzer.calculateAngle(shoulder, elbow, wrist)
                                )
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
                    "Squat" -> FormAnalyzer.analyzeSquat(minKneeAngle, minBackAngle, validFrames)
                    "Push-up" -> FormAnalyzer.analyzePushUp(minElbowAngle, minBodyAngle, validFrames)
                    "Plank" -> FormAnalyzer.analyzePlank(minBodyAngle, validFrames)
                    else -> FormResult(0, emptyList(), "Unsupported workout", false)
                }

                // Save Score to Backend
                if (userId != -1 && validFrames >= 5) {
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
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Spacer(Modifier.height(16.dp))

            // Workout Selector
            Text("Select Workout", fontSize = 18.sp, fontWeight = FontWeight.Bold)
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

            // Upload Box (Only show if not analyzing, or show simple button if result exists)
            if (!isAnalyzing) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(if (formResult == null) 160.dp else 60.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White)
                        .dashedBorder(2.dp, Color.LightGray, 16.dp)
                        .clickable { launcher.launch("video/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        if (formResult == null) "Tap to upload video for analysis" else "Analyze a New Video",
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                }
            } else {
                 Box(Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(Modifier.height(8.dp))
                        Text("Analyzing Form...", color = Color.Gray)
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            SampleWorkoutVideo(selectedWorkout)

            Spacer(Modifier.height(32.dp))

            // Top Scores Section
            Text("Your Top Scores", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            
            if (isLoadingScores) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
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
            Text("Perfect $workout Form", color = Color.White, fontWeight = FontWeight.Bold)
            Text("Watch and learn", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
        }
    }
}

/* ====================== UI COMPONENTS ====================== */

@Composable
fun FormResultSection(workoutName: String, result: FormResult) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(workoutName, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1C1E))
        Spacer(Modifier.height(24.dp))

        // Overall Score Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Overall Form Score", fontSize = 16.sp, color = Color.Black)
            Text("${result.score}/100", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        }
        
        Spacer(Modifier.height(8.dp))
        
        // Progress Bar
        LinearProgressIndicator(
            progress = { result.score / 100f },
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .clip(RoundedCornerShape(6.dp)),
            color = Color(0xFF1976D2),
            trackColor = Color(0xFFEEEEEE),
        )
        Spacer(Modifier.height(4.dp))
        Text(
            if (result.score >= 80) "Good" else "Needs Work", 
            color = Color(0xFF1976D2), 
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(Modifier.height(24.dp))
        Divider(color = Color.LightGray, thickness = 0.5.dp)
        Spacer(Modifier.height(24.dp))

        // Feedback List
        Text(
            "Form Feedback List",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(16.dp))

        result.feedback.forEach { item ->
            FeedbackRow(item)
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(24.dp))

        // Quick Tip
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFFBBDEFB)) // Light Blue
                .padding(16.dp)
        ) {
            Column {
                Text("Quick Tip", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                Text(result.quickTip, color = Color(0xFF0D47A1), fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun FeedbackRow(item: com.example.sweatzone.utils.FeedbackItem) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (item.isPositive) Color(0xFFE3F2FD) else Color(0xFFFFEBEE)), // Blue vs Red bg
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (item.isPositive) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (item.isPositive) Color(0xFF1976D2) else Color(0xFFD32F2F),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(Modifier.width(16.dp))
        Text(item.message, fontSize = 16.sp, color = Color(0xFF1A1C1E))
    }
}

@Composable
fun TopScoreItem(rank: Int, score: ScoreItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFF1976D2)),
                contentAlignment = Alignment.Center
            ) {
                Text("#$rank", color = Color.White, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("Score: ${score.score}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
        label = { Text(name) },
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF1976D2),
            selectedLabelColor = Color.White
        )
    )
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

