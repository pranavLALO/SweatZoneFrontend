package com.example.sweatzone.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Corrected Horizontal Ruler Implementation
@Composable
fun HorizontalRuler(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    minValue: Float,
    maxValue: Float
) {
    val density = LocalDensity.current
    val stepWidth = with(density) { 10.dp.toPx() } // The space between each value

    Canvas(modifier = modifier
        .fillMaxWidth()
        .height(80.dp)
        .pointerInput(Unit) {
            detectDragGestures {
                change, dragAmount ->
                change.consume()
                val delta = -(dragAmount.x / stepWidth)
                val newValue = (value + delta).coerceIn(minValue, maxValue)
                onValueChange(newValue)
            }
        }
    ) { 
        val canvasWidth = size.width
        val centerLineX = canvasWidth / 2

        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.DKGRAY
            textSize = 12.sp.toPx()
            textAlign = android.graphics.Paint.Align.CENTER
        }

        // Draw the ruler lines and numbers
        for (v in minValue.toInt()..maxValue.toInt()) {
            val xPos = centerLineX - (v - value) * stepWidth

            // Only draw lines that are visible on the canvas
            if(xPos > 0 && xPos < canvasWidth) {
                val isMajorLine = v % 5 == 0
                val lineHeight = if (isMajorLine) 40f else 20f
                
                drawLine(
                    color = Color.Gray.copy(alpha = 0.5f),
                    start = Offset(xPos, 0f),
                    end = Offset(xPos, lineHeight),
                    strokeWidth = 2f
                )
                
                if (isMajorLine) {
                    drawContext.canvas.nativeCanvas.drawText(
                        v.toString(),
                        xPos,
                        lineHeight + 30f,
                        paint
                    )
                }
            }
        }

        // Center Indicator Line
        drawLine(
            color = Color(0xFF1C1C1E),
            start = Offset(centerLineX, 0f),
            end = Offset(centerLineX, 50f),
            strokeWidth = 4f
        )
    }
}

// --- Other Reusable Onboarding Components ---

@Composable
fun OnboardingProgressBar(currentStep: Int, totalSteps: Int = 4) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        (1..totalSteps).forEach { step ->
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        if (step < currentStep) Color.LightGray
                        else if (step == currentStep) Color.Black
                        else Color.LightGray,
                        RoundedCornerShape(2.dp)
                    )
            )
            if (step < totalSteps) Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
fun UnitToggleButton(
    options: List<String>,
    selectedOption: String,
    onOptionSelect: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, Color(0xFFEEEEEE))
    ) {
        Row {
            options.forEach { option ->
                val isSelected = selectedOption == option
                Button(
                    onClick = { onOptionSelect(option) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color(0xFF1C1C1E) else Color.Transparent,
                        contentColor = if (isSelected) Color.White else Color.Gray
                    ),
                    shape = RoundedCornerShape(24.dp),
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 40.dp, vertical = 12.dp)
                ) {
                    Text(text = option, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
fun OnboardingFooter(onBack: () -> Unit, onNext: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            color = Color.White,
            modifier = Modifier
                .size(56.dp)
                .clickable(onClick = onBack)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1C1C1E)),
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Next", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next")
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, "Next", tint = Color.Gray)
            }
        }
    }
}