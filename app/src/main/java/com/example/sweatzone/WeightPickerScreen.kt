package com.example.sweatzone

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.OnboardingFooter
import com.example.sweatzone.ui.components.OnboardingProgressBar
import com.example.sweatzone.ui.components.UnitToggleButton
import com.example.sweatzone.ui.theme.SweatzoneTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun WeightPickerScreen(navController: NavController) {
    // State: We use Int for easier snapping logic
    var weight by remember { mutableIntStateOf(70) }
    var unit by remember { mutableStateOf("kg") }

    val primaryDark = Color(0xFF1C1C1E)
    val cardYellow = Color(0xFFFFFBE6)

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Configuration
    val minWeight = if (unit == "kg") 20 else 44
    val maxWeight = if (unit == "kg") 200 else 440
    val stepCount = maxWeight - minWeight

    // Width of each ruler line item (space between lines)
    val itemWidthDp = 12.dp

    // Initialize the list state to the starting weight
    // We calculate the index relative to the minWeight
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (weight - minWeight).coerceAtLeast(0)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingProgressBar(currentStep = 1)

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "What is your\nweight?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = primaryDark,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            UnitToggleButton(
                options = listOf("lb", "kg"),
                selectedOption = unit,
                onOptionSelect = { newUnit ->
                    if (unit != newUnit) {
                        // Convert value
                        val newWeightVal = if (newUnit == "kg") (weight / 2.20462f) else (weight * 2.20462f)
                        val newWeightInt = newWeightVal.roundToInt().coerceIn(
                            if (newUnit == "kg") 20 else 44,
                            if (newUnit == "kg") 200 else 440
                        )

                        unit = newUnit
                        weight = newWeightInt

                        // Snap scroll to new converted value
                        val newMin = if (newUnit == "kg") 20 else 44
                        scope.launch {
                            listState.scrollToItem((newWeightInt - newMin).coerceAtLeast(0))
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // RULER CONTAINER
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(cardYellow, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {

                    // 1. The Ruler (Takes up most space)
                    // BoxWithConstraints is crucial to calculate padding correctly and avoid crashes
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        val boxWidth = maxWidth
                        // Add padding so the first and last items can reach the center
                        val horizontalPadding = (boxWidth / 2) - (itemWidthDp / 2)

                        // Calculate which index is currently in the center
                        val centerIndex by remember {
                            derivedStateOf {
                                val itemWidthPx = with(density) { itemWidthDp.toPx() }
                                val centerOffset = with(density) { boxWidth.toPx() / 2 }

                                val visibleIndex = listState.firstVisibleItemIndex
                                val visibleOffset = listState.firstVisibleItemScrollOffset

                                // Calculate total scroll offset + center of screen
                                val currentScroll = (visibleIndex * itemWidthPx) + visibleOffset
                                val centerPoint = currentScroll + centerOffset

                                // Convert back to index
                                val index = ((currentScroll + (itemWidthPx / 2)) / itemWidthPx).roundToInt()
                                index.coerceIn(0, stepCount)
                            }
                        }

                        // Update the main state when scrolling happens
                        LaunchedEffect(centerIndex) {
                            weight = minWeight + centerIndex
                        }

                        // Central Indicator Line (The dark bold line in the middle)
                        Box(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .width(4.dp)
                                .height(50.dp)
                                .background(primaryDark, RoundedCornerShape(2.dp))
                                .zIndex(1f) // Ensure it's on top
                        )

                        // The Scrolling List
                        LazyRow(
                            state = listState,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(stepCount + 1) { index ->
                                val currentVal = minWeight + index
                                val isMajor = currentVal % 10 == 0
                                val isMedium = currentVal % 5 == 0

                                // Height of lines based on significance
                                val lineHeight = when {
                                    isMajor -> 40.dp
                                    isMedium -> 25.dp
                                    else -> 15.dp
                                }

                                val lineColor = if (isMajor) Color.Black else Color.Gray.copy(alpha = 0.5f)

                                Box(
                                    modifier = Modifier
                                        .width(itemWidthDp)
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // The Line
                                        Box(
                                            modifier = Modifier
                                                .width(1.5.dp)
                                                .height(lineHeight)
                                                .background(lineColor, RoundedCornerShape(1.dp))
                                        )

                                        // The Number (only for major ticks like 60, 70)
                                        if (isMajor) {
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = currentVal.toString(),
                                                fontSize = 10.sp,
                                                color = Color.Gray.copy(alpha = 0.7f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 2. The Large Weight Display Text
                    Text(
                        text = "$weight",
                        fontSize = 70.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryDark,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingFooter(
            onBack = { navController.popBackStack() },
            onNext = { navController.navigate(Screen.HeightPicker.route) }
        )
    }
}

// Extension to ensure zIndex works if needed
fun Modifier.zIndex(zIndex: Float) = this.then(
    object : androidx.compose.ui.layout.LayoutModifier {
        override fun androidx.compose.ui.layout.MeasureScope.measure(
            measurable: androidx.compose.ui.layout.Measurable,
            constraints: androidx.compose.ui.unit.Constraints
        ): androidx.compose.ui.layout.MeasureResult {
            val placeable = measurable.measure(constraints)
            return layout(placeable.width, placeable.height) {
                placeable.place(0, 0, zIndex = zIndex)
            }
        }
    }
)

@Preview(showBackground = true)
@Composable
fun WeightPickerScreenPreview() {
    SweatzoneTheme {
        WeightPickerScreen(rememberNavController())
    }
}
