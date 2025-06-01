package com.joincoded.bankapi.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    viewModel: BankViewModel,
    navController: NavController
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    // Handle success message
    LaunchedEffect(viewModel.successMessage) {
        if (viewModel.successMessage?.contains("Registration successful") == true) {
            navController.navigate(AppDestinations.lOGIN) {
                popUpTo(AppDestinations.SIGNUP) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            // Logo
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C3E50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "YB",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            Text(
                text = "yalla banking",
                fontSize = 24.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Create your account",
                fontSize = 16.sp,
                color = Color(0xFF666666)
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Username Field
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Username",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    placeholder = {
                        Text(
                            text = "Choose a username",
                            color = Color(0xFFAAAAAA)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2C3E50),
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Password Field
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Password",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = {
                        Text(
                            text = "Create a password",
                            color = Color(0xFFAAAAAA)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2C3E50),
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Confirm Password Field
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Confirm Password",
                    fontSize = 14.sp,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = {
                        Text(
                            text = "Confirm your password",
                            color = Color(0xFFAAAAAA)
                        )
                    },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2C3E50),
                        unfocusedBorderColor = Color(0xFFDDDDDD),
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp),
                    isError = confirmPassword.isNotEmpty() && password != confirmPassword
                )

                if (confirmPassword.isNotEmpty() && password != confirmPassword) {
                    Text(
                        text = "Passwords do not match",
                        color = Color(0xFFD32F2F),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Error/Success Messages
            viewModel.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            viewModel.successMessage?.let { success ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E8))
                ) {
                    Text(
                        text = success,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sign Up Button
            Button(
                onClick = {
                    if (password != confirmPassword) {
                        viewModel.errorMessage = "Passwords do not match"
                        return@Button
                    }
                    if (password.length < 6) {
                        viewModel.errorMessage = "Password must be at least 6 characters"
                        return@Button
                    }
                    viewModel.register(username, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = username.isNotBlank() &&
                        password.isNotBlank() &&
                        confirmPassword.isNotBlank() &&
                        password == confirmPassword &&
                        !viewModel.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C3E50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Create Account",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Back to Login
            TextButton(
                onClick = { navController.navigate(AppDestinations.lOGIN) }
            ) {
                Text(
                    text = "Already have an account? Sign in",
                    color = Color(0xFF666666),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // Copyright
            Text(
                text = "© 2025 yalla banking",
                fontSize = 12.sp,
                color = Color(0xFFAAAAAA),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}