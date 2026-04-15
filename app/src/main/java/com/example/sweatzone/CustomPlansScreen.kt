package com.example.sweatzone

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.navigation.NavController
import com.example.sweatzone.data.dto.CustomRoutine
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CustomPlansScreen(navController: NavController, userViewModel: UserViewModel) {
    val customRoutines by userViewModel.customRoutines.collectAsState()
    val userBadges by userViewModel.userBadges.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedRoutineForMenu by remember { mutableStateOf<CustomRoutine?>(null) }

    val pagerState = rememberPagerState(pageCount = { customRoutines.size })

    LaunchedEffect(Unit) {
        userViewModel.fetchCustomRoutines()
        userViewModel.fetchUserBadges()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Master Plans", color = Color.White, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("workout_library") }) {
                        Icon(Icons.Default.Add, contentDescription = "Create New", tint = Color(0xFFE0FF63))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 1. CAROUSEL SECTION
            if (customRoutines.isEmpty() && !isLoading) {
                EmptyPlansState(onNavigateToLibrary = { navController.navigate("workout_library") })
            } else {
                Text(
                    text = "SWIPE TO BROWSE",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.height(16.dp))

                HorizontalPager(
                    state = pagerState,
                    contentPadding = PaddingValues(horizontal = 48.dp),
                    modifier = Modifier.height(400.dp)
                ) { page ->
                    val routine = customRoutines[page]
                    
                    // Card Scaling Animation Logic
                    val pageOffset = (
                        (pagerState.currentPage - page) + pagerState.currentPageOffsetFraction
                    ).absoluteValue

                    val scale = lerp(
                        start = 0.85f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )
                    
                    val alpha = lerp(
                        start = 0.5f,
                        stop = 1f,
                        fraction = 1f - pageOffset.coerceIn(0f, 1f)
                    )

                    RoutineCarouselCard(
                        routine = routine,
                        scale = scale,
                        alpha = alpha,
                        onStart = {
                            // Navigate to Custom Workout Player
                            navController.navigate("custom_workout/${routine.id}")
                        },
                        onManage = {
                            selectedRoutineForMenu = routine
                            showBottomSheet = true
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 2. MEDAL SHELF SECTION
            Text(
                text = "ACHIEVEMENT MEDALS",
                color = Color.White,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp,
                modifier = Modifier.align(Alignment.Start).padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            if (userBadges.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Complete custom sessions to earn medals!",
                        color = Color.LightGray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    userBadges.forEach { badge ->
                        BadgeItem(badge)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }

    // MANAGEMENT BOTTOM SHEET
    if (showBottomSheet && selectedRoutineForMenu != null) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = Color(0xFF1E1E1E),
            dragHandle = { BottomSheetDefaults.DragHandle(color = Color.Gray) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 48.dp, start = 24.dp, end = 24.dp)
            ) {
                Text(
                    text = selectedRoutineForMenu?.routine_name ?: "Manage Routine",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(24.dp))
                
                MenuOption(
                    icon = Icons.Default.Edit,
                    label = "Edit Exercises",
                    sublabel = "Add or remove workouts from this plan",
                    onClick = {
                        showBottomSheet = false
                        // TODO: Implement Edit Navigation
                    }
                )
                MenuOption(
                    icon = Icons.Default.DriveFileRenameOutline,
                    label = "Rename Plan",
                    onClick = {
                        showBottomSheet = false
                        // TODO: Implement Rename Dialog
                    }
                )
                MenuOption(
                    icon = Icons.Default.Delete,
                    label = "Delete Plan",
                    color = Color(0xFFFF5252),
                    onClick = {
                        showBottomSheet = false
                        // TODO: Implement Delete Action
                    }
                )
            }
        }
    }
}

@Composable
fun RoutineCarouselCard(
    routine: CustomRoutine,
    scale: Float,
    alpha: Float,
    onStart: () -> Unit,
    onManage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                this.alpha = alpha
            },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Gradient Glow
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFE0FF63).copy(alpha = 0.05f),
                                Color.Transparent
                            )
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onManage) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Manage", tint = Color.Gray)
                    }
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = Color(0xFFE0FF63),
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = routine.routine_name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "${routine.exercises.size} EXERCISES",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }

                Button(
                    onClick = onStart,
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text("START SESSION", color = Color.Black, fontWeight = FontWeight.Black)
                }
            }
        }
    }
}

@Composable
fun BadgeItem(badge: com.example.sweatzone.data.dto.BadgeDto) {
    val tierColor = when (badge.tier.lowercase()) {
        "gold" -> Color(0xFFFFD700)
        "silver" -> Color(0xFFC0C0C0)
        else -> Color(0xFFCD7F32)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.05f))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(tierColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MilitaryTech,
                contentDescription = null,
                tint = tierColor,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = badge.message, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "${badge.routine_name} • ${badge.type}", color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun MenuOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    sublabel: String? = null,
    color: Color = Color.White,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, color = color, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            if (sublabel != null) {
                Text(text = sublabel, color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun EmptyPlansState(onNavigateToLibrary: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.DesignServices, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(64.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No plans found",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            "Hand-pick your favorite exercises to create your first Master Plan.",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onNavigateToLibrary, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63))) {
            Text("EXPLORE LIBRARY", color = Color.Black)
        }
    }
}
