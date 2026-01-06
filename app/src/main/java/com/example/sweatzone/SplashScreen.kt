package com.example.sweatzone

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.sweatzone.ui.theme.SweatzoneTheme

@Composable
fun SplashScreen(modifier: Modifier = Modifier, navController: NavController) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color(0xFF231F2E) // Dark purple color
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.5f)),
                color = Color.Transparent,
                modifier = Modifier.size(120.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.lo),
                    contentDescription = "Fitness Icon",
                    modifier = Modifier
                        .size(100.dp)
                        .padding(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                textAlign = TextAlign.Center,
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Light)) {
                        append("Start your\n\n")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFE0FF63), // Yellowish color
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                    ) {
                        append("Fitness ")
                    }
                    withStyle(style = SpanStyle(color = Color.White, fontSize = 36.sp, fontWeight = FontWeight.Light)) {
                        append("Journey")
                    }
                }
            )
            Spacer(modifier = Modifier.height(48.dp))
            Button(
                onClick = { navController.navigate(Screen.Login.route) },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A3645)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Login", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate(Screen.Register.route) },
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(text = "Register", color = Color.Black, fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SweatzoneTheme {
        SplashScreen(navController = rememberNavController())
    }
}
