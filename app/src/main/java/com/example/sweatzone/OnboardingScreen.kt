package com.example.sweatzone

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.theme.SweatzoneTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun OnboardingScreen(navController: NavController) {
    val darkPurple = Color(0xFF3A3A5A)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = darkPurple
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Decorative circles in the background
            Circle(color = Color.White.copy(alpha = 0.05f), radius = 80.dp, offsetX = 300.dp, offsetY = 100.dp)
            Circle(color = Color.White.copy(alpha = 0.05f), radius = 20.dp, offsetX = 330.dp, offsetY = 150.dp)
            Circle(color = Color.White.copy(alpha = 0.05f), radius = 40.dp, offsetX = 150.dp, offsetY = 300.dp)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Spacer(modifier = Modifier.height(80.dp))

                // Main Title Text
                Text(
                    text = "Start your\nFitness Journey",
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    lineHeight = 50.sp
                )

                Spacer(modifier = Modifier.height(60.dp))

                // Decorative Scribble Arrow
                ScribbleArrow()

                Spacer(modifier = Modifier.weight(1f))

                // Subtitle Text
                Text(
                    text = "Start your fitness journey\nwith our app's guidance and support.",
                    fontSize = 16.sp,
                    color = Color.White.copy(alpha = 0.8f),
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(40.dp))

                // The new, animated swipe button
                SwipeToStartButton(
                    onSwiped = {
                        // Navigate to the next screen in the onboarding flow
                        navController.navigate(Screen.Login.route) {
                            // This removes the onboarding screen from the back stack, so the user can't go back to it.
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}

// --- Reusable and Internal Components for this Screen ---

@Composable
private fun SwipeToStartButton(onSwiped: () -> Unit) {
    val density = LocalDensity.current
    val coroutineScope = rememberCoroutineScope()

    // Dimensions
    val buttonHeight = 80.dp
    val thumbSize = 64.dp
    val padding = 8.dp

    // State
    var containerWidthPx by remember { mutableFloatStateOf(0f) }
    val offsetX = remember { Animatable(0f) }

    // Calculate the maximum distance the thumb can be swiped
    val maxSwipeDistance = remember(containerWidthPx) {
        if (containerWidthPx == 0f) 0f else {
            containerWidthPx - with(density) { (thumbSize + (padding * 2)).toPx() }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .clip(RoundedCornerShape(40.dp))
            .background(Color.Black.copy(alpha = 0.2f))
            .onSizeChanged { containerWidthPx = it.width.toFloat() },
        contentAlignment = Alignment.CenterStart
    ) {
        // "Lets start" Text and Arrows (sits behind the swipe thumb)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Lets start",
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                modifier = Modifier.padding(end = 16.dp)
            )
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "", tint = Color.White.copy(alpha = 0.8f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "", tint = Color.White.copy(alpha = 0.5f))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "", tint = Color.White.copy(alpha = 0.3f))
        }

        // Draggable Swipe Thumb (White Circle)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), 0) }
                .padding(padding)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White)
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        coroutineScope.launch {
                            // Update offset during drag, ensuring it stays within the defined bounds
                            val newOffset = (offsetX.value + delta).coerceIn(0f, maxSwipeDistance)
                            offsetX.snapTo(newOffset)
                        }
                    },
                    onDragStopped = {
                        // Check if the swipe passed the success threshold (e.g., 80% of the width)
                        if (offsetX.value > maxSwipeDistance * 0.8f) {
                            // Animate to the end and trigger the navigation action
                            coroutineScope.launch {
                                offsetX.animateTo(maxSwipeDistance, animationSpec = spring(stiffness = Spring.StiffnessLow))
                                onSwiped()
                            }
                        } else {
                            // If not swiped far enough, animate back to the start
                            coroutineScope.launch {
                                offsetX.animateTo(0f, animationSpec = spring(stiffness = Spring.StiffnessMedium))
                            }
                        }
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Swipe to Start",
                tint = Color.Black
            )
        }
    }
}

// THIS IS THE CORRECTED CIRCLE COMPOSABLE
@Composable
private fun Circle(color: Color, radius: Dp, offsetX: Dp, offsetY: Dp) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        // Convert Dp to Float pixels inside the DrawScope
        val center = Offset(offsetX.toPx(), offsetY.toPx())
        drawCircle(color, radius = radius.toPx(), center = center)
    }
}

@Composable
private fun ScribbleArrow() {
    Canvas(modifier = Modifier.size(60.dp)) {
        val path = Path().apply {
            moveTo(0f, size.height * 0.5f)
            cubicTo(
                size.width * 0.2f, size.height * 0.1f,
                size.width * 0.8f, size.height * 0.3f,
                size.width, size.height * 0.7f
            )
        }
        drawPath(
            path = path,
            color = Color.White.copy(alpha = 0.5f),
            style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    SweatzoneTheme {
        OnboardingScreen(rememberNavController())
    }
}
