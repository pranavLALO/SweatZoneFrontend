package com.example.sweatzone

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sweatzone.data.api.RetrofitClient
import com.example.sweatzone.data.dto.ForgotPasswordRequest
import com.example.sweatzone.data.dto.ResetPasswordRequest
import com.example.sweatzone.data.dto.VerifyOtpRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Forgot Password", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Icon(
                imageVector = Icons.Default.Email,
                contentDescription = "Email",
                tint = Color(0xFFE0FF63),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Account Recovery",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Enter your registered email address to receive a secure 6-digit recovery code.",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email Address", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0FF63),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (email.isBlank()) {
                        Toast.makeText(context, "Please enter your email", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        try {
                            val response = RetrofitClient.api.forgotPassword(ForgotPasswordRequest(email))
                            if (response.isSuccessful && response.body()?.status == true) {
                                Toast.makeText(context, response.body()?.message ?: "OTP Sent", Toast.LENGTH_LONG).show()
                                navController.navigate("verify_otp/$email")
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Failed to find email", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show()
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text("Send Recovery Code", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyOtpScreen(navController: NavController, email: String) {
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Verify Code", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock",
                tint = Color(0xFFE0FF63),
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Enter 6-Digit Code",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "We sent a code to $email",
                color = Color.Gray,
                fontSize = 14.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            Spacer(modifier = Modifier.height(40.dp))

            OutlinedTextField(
                value = otp,
                onValueChange = { if (it.length <= 6) otp = it },
                label = { Text("000000", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0FF63),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (otp.length != 6) {
                        Toast.makeText(context, "Please enter full 6-digit OTP", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        try {
                            val response = RetrofitClient.api.verifyOtp(VerifyOtpRequest(email, otp))
                            if (response.isSuccessful && response.body()?.status == true) {
                                Toast.makeText(context, response.body()?.message ?: "Verified!", Toast.LENGTH_SHORT).show()
                                navController.navigate("reset_password/$email/$otp") {
                                    popUpTo("verify_otp/$email") { inclusive = true }
                                }
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Invalid OTP", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show()
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text("Verify Code", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(navController: NavController, email: String, otp: String) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New Password", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF121212))
            )
        },
        containerColor = Color(0xFF121212)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Create New Password",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0FF63),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Re-enter Password", color = Color.Gray) },
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFE0FF63),
                    unfocusedBorderColor = Color.DarkGray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (newPassword.isBlank() || confirmPassword.isBlank()) {
                        Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (newPassword.length < 6) {
                        Toast.makeText(context, "Password must be > 6 chars", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (newPassword != confirmPassword) {
                        Toast.makeText(context, "Passwords do not match!", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    scope.launch {
                        isLoading = true
                        try {
                            val response = RetrofitClient.api.resetPassword(ResetPasswordRequest(email, otp, newPassword))
                            if (response.isSuccessful && response.body()?.status == true) {
                                Toast.makeText(context, response.body()?.message ?: "Success!", Toast.LENGTH_LONG).show()
                                // Navigate straight to Login, kill all previous auth stacks
                                navController.navigate("login") {
                                    popUpTo(0)
                                }
                            } else {
                                Toast.makeText(context, response.body()?.message ?: "Failed to reset", Toast.LENGTH_LONG).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Network error", Toast.LENGTH_LONG).show()
                        }
                        isLoading = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0FF63)),
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = Color.Black, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save & Login", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
