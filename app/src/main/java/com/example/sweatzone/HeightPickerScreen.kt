package com.example.sweatzone

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.components.OnboardingFooter
import com.example.sweatzone.ui.components.OnboardingProgressBar
import com.example.sweatzone.ui.components.UnitToggleButton
import com.example.sweatzone.ui.theme.SweatzoneTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun HeightPickerScreen(navController: NavController, userViewModel: UserViewModel) {
    // State: Use Int for easier list indexing/snapping
    var height by remember { mutableIntStateOf(170) }
    var unit by remember { mutableStateOf("cm") }

    val primaryDark = Color(0xFF1C1C1E)
    val cardBlue = Color(0xFFE0F7FA) // Pale Blue matching the image

    val density = LocalDensity.current
    val scope = rememberCoroutineScope()

    // Ruler Configuration
    val itemWidthDp = 12.dp // Space between lines

    // Dynamic Range Calculation
    val minHeight = if (unit == "cm") 120 else 48
    val maxHeight = if (unit == "cm") 220 else 86
    val stepCount = maxHeight - minHeight

    // Scroll State
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = (height - minHeight).coerceAtLeast(0)
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
            OnboardingProgressBar(currentStep = 2)

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "What is your\nheight?",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = primaryDark,
                textAlign = TextAlign.Center,
                lineHeight = 34.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            UnitToggleButton(
                options = listOf("inches", "cm"),
                selectedOption = unit,
                onOptionSelect = { newUnit ->
                    if (unit != newUnit) {
                        // 1. Convert logic
                        val newHeightVal = if (newUnit == "cm") (height * 2.54f) else (height / 2.54f)
                        val newHeightInt = newHeightVal.roundToInt().coerceIn(
                            if (newUnit == "cm") 120 else 48,
                            if (newUnit == "cm") 220 else 86
                        )

                        // 2. Update State
                        unit = newUnit
                        height = newHeightInt

                        // 3. Snap list to new position
                        val newMin = if (newUnit == "cm") 120 else 48
                        val newIndex = (newHeightInt - newMin).coerceAtLeast(0)

                        scope.launch {
                            listState.scrollToItem(newIndex)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // The Height Picker Card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp) // Taller to fit number on top + ruler
                    .background(cardBlue, RoundedCornerShape(32.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(30.dp))

                    // 1. Big Number Display (Top)
                    Text(
                        text = "$height",
                        fontSize = 80.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryDark,
                        letterSpacing = (-2).sp
                    )

                    // 2. The Ruler (Middle/Bottom)
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        val boxWidth = maxWidth
                        // Calculate padding so start/end items can reach center
                        val horizontalPadding = (boxWidth / 2) - (itemWidthDp / 2)

                        // Calculate current value based on scroll position
                        val centerIndex by remember {
                            derivedStateOf {
                                val itemWidthPx = with(density) { itemWidthDp.toPx() }
                                val centerOffset = with(density) { boxWidth.toPx() / 2 }

                                val visibleIndex = listState.firstVisibleItemIndex
                                val visibleOffset = listState.firstVisibleItemScrollOffset

                                val currentScroll = (visibleIndex * itemWidthPx) + visibleOffset
                                val index = ((currentScroll + (itemWidthPx/2)) / itemWidthPx).roundToInt()

                                index.coerceIn(0, stepCount)
                            }
                        }

                        // Update height state when scrolling
                        LaunchedEffect(centerIndex) {
                            height = minHeight + centerIndex
                        }

                        // Center Indicator Line (Dark line)
                        Box(
                            modifier = Modifier
                                .width(2.5.dp)
                                .height(60.dp)
                                .background(primaryDark, RoundedCornerShape(10.dp))
                                .align(Alignment.Center)
                                .zIndex(1f)
                        )

                        // The Scrollable Ticks
                        LazyRow(
                            state = listState,
                            flingBehavior = rememberSnapFlingBehavior(lazyListState = listState),
                            contentPadding = PaddingValues(horizontal = horizontalPadding),
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            items(stepCount + 1) { index ->
                                val currentVal = minHeight + index
                                val isMajor = currentVal % 10 == 0
                                val isMedium = currentVal % 5 == 0

                                val lineHeight = if (isMajor) 40.dp else if (isMedium) 25.dp else 15.dp
                                val lineColor = if (isMajor) Color.Gray else Color.Gray.copy(alpha = 0.5f)

                                Box(
                                    modifier = Modifier
                                        .width(itemWidthDp)
                                        .fillMaxHeight(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        // The tick line
                                        Box(
                                            modifier = Modifier
                                                .width(1.5.dp)
                                                .height(lineHeight)
                                                .background(lineColor, RoundedCornerShape(1.dp))
                                        )

                                        // The number label for major ticks
                                        if (isMajor) {
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = currentVal.toString(),
                                                fontSize = 12.sp,
                                                color = Color.Gray,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Unit Label (Bottom)
                    Text(
                        text = unit,
                        fontSize = 16.sp,
                        color = primaryDark,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OnboardingFooter(
            onBack = { navController.popBackStack() },
            onNext = { 
                userViewModel.setHeight(height) // Save to VM
                navController.navigate(Screen.GoalSelection.route) 
            }
        )
    }
}



@Preview(showBackground = true)
@Composable
fun HeightPickerScreenPreview() {
    SweatzoneTheme {
        HeightPickerScreen(rememberNavController(), UserViewModel())
    }
}
