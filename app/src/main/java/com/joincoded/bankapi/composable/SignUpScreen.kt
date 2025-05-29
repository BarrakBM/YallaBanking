package com.joincoded.bankapi.composable


import androidx.compose.material.icons.filled.Person
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.viewmodel.BankViewModel

import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navOptions
//
//@Composable
////,onSignInClick: () -> Unit
//fun YallaBankingSignUpScreen(viewModel: BankViewModel = viewModel(),modifier: Modifier = Modifier) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    val hasMinLength = password.length >= 6
//    val hasUppercase = password.any { it.isUpperCase() }
//    val hasLowercase = password.any { it.isLowerCase() }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        // Header Section
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF2C3E50))
//                .padding(16.dp)
//        ) {
//            IconButton(
//                onClick = { /* Handle back */ },
//                modifier = Modifier.align(Alignment.CenterStart)
//            ) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Back",
//                    tint = Color.White
//                )
//            }
//
//            Text(
//                text = "Sign Up",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Medium,
//                modifier = Modifier.align(Alignment.Center)
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF2C3E50))
//                .padding(bottom = 32.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//            Box(
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//                    .background(Color(0xFF34495E)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Bank",
//                    tint = Color.White,
//                    modifier = Modifier.size(32.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "yalla banking",
//                color = Color.White,
//                fontSize = 24.sp,
//                fontWeight = FontWeight.Normal
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(24.dp)
//        ) {
//            OutlinedTextField(
//                value = username,
//                onValueChange = { username = it },
//                label = { Text("Username") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF2C3E50),
//                    unfocusedBorderColor = Color(0xFFDDDDDD),
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                ),
//                shape = RoundedCornerShape(8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF2C3E50),
//                    unfocusedBorderColor = Color(0xFFDDDDDD),
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                ),
//                shape = RoundedCornerShape(8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            PasswordRequirement("At least 6 characters", hasMinLength)
//            Spacer(modifier = Modifier.height(8.dp))
//            PasswordRequirement("1 uppercase letter", hasUppercase)
//            Spacer(modifier = Modifier.height(8.dp))
//            PasswordRequirement("1 lowercase letter", hasLowercase)
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            Button(
//                onClick = {
//                    if (hasMinLength && hasUppercase && hasLowercase) {
//                        viewModel.signup(username, password)
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                Text(
//                    text = "Sign up",
//                    color = Color.White,
//                    fontSize = 16.sp,
//                    fontWeight = FontWeight.Medium
//                )
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Text("Already have an account? ", color = Color(0xFF666666), fontSize = 14.sp)
//                Text(
//                    text = "Sign in",
//                    color = Color(0xFF2C3E50),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .padding(start = 4.dp)
//                        .clickable { /* Navigate to sign in */ }
//                )
//            }
////            Row(
////                modifier = Modifier.fillMaxWidth(),
////                horizontalArrangement = Arrangement.Center
////            ) {
////                Text("Already have an account? ", color = Color(0xFF666666), fontSize = 14.sp)
////                TextButton(onClick = onSignInClick) {
////                    Text("Sign in", color = Color(0xFF2C3E50), fontSize = 14.sp)
////                }
////            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                text = "© 2025 yalla banking. All rights reserved.",
//                fontSize = 12.sp,
//                color = Color(0xFFAAAAAA),
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun PasswordRequirement(text: String, isMet: Boolean) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        RadioButton(
//            selected = isMet,
//            onClick = null,
//            colors = RadioButtonDefaults.colors(
//                selectedColor = if (isMet) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
//                unselectedColor = Color(0xFFDDDDDD)
//            )
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = text,
//            fontSize = 14.sp,
//            color = if (isMet) Color(0xFF4CAF50) else Color(0xFF666666)
//        )
//    }
//}


//
//@Composable
//fun YallaBankingSignUpScreen(
//    //= viewModel()
//    //viewModel: BankViewModel= viewModel(),
//    viewModel: BankViewModel,
//    modifier: Modifier = Modifier,
//    onNavigatorTogo: ()-> Unit
//) {
//    var username by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//
//    val hasMinLength = password.length >= 6
//    val hasUppercase = password.any { it.isUpperCase() }
//    val hasLowercase = password.any { it.isLowerCase() }
//
//    val isLoading by remember { viewModel::isLoading }
//    val errorMessage by remember { viewModel::errorMessage }
//    val successMessage by remember { viewModel::successMessage }
//
//    // Clear form on successful registration
//    LaunchedEffect(successMessage) {
//        if (!successMessage.isNullOrEmpty()) {
//            username = ""
//            password = ""
//            navController.navigate(AppDestinations.lOGIN) {
//                popUpTo(AppDestinations.SIGNUP) { inclusive = true }
//            }
//        }
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color(0xFFF5F5F5))
//    ) {
//        // Header
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF2C3E50))
//                .padding(16.dp)
//        ) {
//            IconButton(
//                onClick = { /* Navigate back */ },
//                modifier = Modifier.align(Alignment.CenterStart)
//            ) {
//                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
//            }
//
////            Text(
////                text = "Sign Up",
////                color = Color.White,
////                fontSize = 18.sp,
////                fontWeight = FontWeight.Medium,
////                modifier = Modifier.align(Alignment.Center)
////            )
//            Text(
//                text = "Sign in",
//                color = Color(0xFF2C3E50),
//                fontSize = 14.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier
//                    .padding(start = 4.dp)
//                    .clickable {
//                        navController.navigate(AppDestinations.lOGIN)
//                    }
//            )
//        }
//
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color(0xFF2C3E50))
//                .padding(bottom = 32.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Box(
//                modifier = Modifier
//                    .size(80.dp)
//                    .clip(CircleShape)
//                    .background(Color(0xFF34495E)),
//                contentAlignment = Alignment.Center
//            ) {
//                Icon(
//                    Icons.Default.Person,
//                    contentDescription = "Bank",
//                    tint = Color.White,
//                    modifier = Modifier.size(32.dp)
//                )
//            }
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Text(
//                text = "yalla banking",
//                color = Color.White,
//                fontSize = 24.sp
//            )
//        }
//
//        Column(modifier = Modifier.padding(24.dp)) {
//            OutlinedTextField(
//                value = username,
//                onValueChange = { username = it },
//                label = { Text("Username") },
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF2C3E50),
//                    unfocusedBorderColor = Color(0xFFDDDDDD),
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                ),
//                shape = RoundedCornerShape(8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            OutlinedTextField(
//                value = password,
//                onValueChange = { password = it },
//                label = { Text("Password") },
//                visualTransformation = PasswordVisualTransformation(),
//                modifier = Modifier.fillMaxWidth(),
//                colors = OutlinedTextFieldDefaults.colors(
//                    focusedBorderColor = Color(0xFF2C3E50),
//                    unfocusedBorderColor = Color(0xFFDDDDDD),
//                    focusedContainerColor = Color.White,
//                    unfocusedContainerColor = Color.White
//                ),
//                shape = RoundedCornerShape(8.dp)
//            )
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            PasswordRequirement("At least 6 characters", hasMinLength)
//            Spacer(modifier = Modifier.height(8.dp))
//            PasswordRequirement("1 uppercase letter", hasUppercase)
//            Spacer(modifier = Modifier.height(8.dp))
//            PasswordRequirement("1 lowercase letter", hasLowercase)
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            if (errorMessage != null) {
//                Text(
//                    text = errorMessage ?: "",
//                    color = Color.Red,
//                    fontSize = 14.sp,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//            }
//
//            if (successMessage != null) {
//                Text(
//                    text = successMessage ?: "",
//                    color = Color(0xFF4CAF50),
//                    fontSize = 14.sp,
//                    modifier = Modifier.padding(bottom = 8.dp)
//                )
//            }
//
//            Button(
//                onClick = {
//                    if (hasMinLength && hasUppercase && hasLowercase && !isLoading) {
//                        viewModel.register(username, password)
//                    }
//                },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp),
//                enabled = !isLoading,
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
//                shape = RoundedCornerShape(8.dp)
//            ) {
//                if (isLoading) {
//                    CircularProgressIndicator(
//                        color = Color.White,
//                        strokeWidth = 2.dp,
//                        modifier = Modifier.size(24.dp)
//                    )
//                } else {
//                    Text(
//                        text = "Sign up",
//                        color = Color.White,
//                        fontSize = 16.sp,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//            }
//
//            Spacer(modifier = Modifier.height(24.dp))
//
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Text("Already have an account? ", color = Color(0xFF666666), fontSize = 14.sp)
//                Text(
//                    text = "Sign in",
//                    color = Color(0xFF2C3E50),
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                    modifier = Modifier
//                        .padding(start = 4.dp)
//                        .clickable { /* Navigate to Sign In */ }
//                )
//            }
//
//            Spacer(modifier = Modifier.weight(1f))
//
//            Text(
//                text = "© 2025 yalla banking. All rights reserved.",
//                fontSize = 12.sp,
//                color = Color(0xFFAAAAAA),
//                textAlign = TextAlign.Center,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(bottom = 16.dp)
//            )
//        }
//    }
//}
//
//@Composable
//fun PasswordRequirement(text: String, isMet: Boolean) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        RadioButton(
//            selected = isMet,
//            onClick = null,
//            colors = RadioButtonDefaults.colors(
//                selectedColor = if (isMet) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
//                unselectedColor = Color(0xFFDDDDDD)
//            )
//        )
//        Spacer(modifier = Modifier.width(8.dp))
//        Text(
//            text = text,
//            fontSize = 14.sp,
//            color = if (isMet) Color(0xFF4CAF50) else Color(0xFF666666)
//        )
//    }
//}
//

@Composable
fun YallaBankingSignUpScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier,
    onNavigatorTogo: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val hasMinLength = password.length >= 6
    val hasUppercase = password.any { it.isUpperCase() }
    val hasLowercase = password.any { it.isLowerCase() }

    val isLoading = viewModel.isLoading
    val errorMessage = viewModel.errorMessage
    val successMessage = viewModel.successMessage

    LaunchedEffect(successMessage) {
        if (!successMessage.isNullOrEmpty()) {
            username = ""
            password = ""
            onNavigatorTogo() // navigate to complete profile screen
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(24.dp)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF2C3E50))
                .padding(16.dp)
        ) {
            IconButton(
                onClick = { /* Handle back */ },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }

            Text(
                text = "Sign Up",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordRequirement("At least 6 characters", hasMinLength)
        PasswordRequirement("1 uppercase letter", hasUppercase)
        PasswordRequirement("1 lowercase letter", hasLowercase)

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (hasMinLength && hasUppercase && hasLowercase) {
                    viewModel.register(username, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Sign up")
        }

        if (!errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = errorMessage, color = Color.Red)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Already have an account? ")
            Text(
                "Sign in",
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {
                    // You can also add another callback here if needed.
                }
            )
        }
    }
}

@Composable
fun PasswordRequirement(text: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(
            selected = isMet,
            onClick = null,
            colors = RadioButtonDefaults.colors(
                selectedColor = if (isMet) Color(0xFF4CAF50) else Color(0xFFDDDDDD),
                unselectedColor = Color(0xFFDDDDDD)
            )
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (isMet) Color(0xFF4CAF50) else Color(0xFF666666)
        )
    }
}

