package com.joincoded.bankapi.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.accountInformationDTO
import com.joincoded.bankapi.viewmodel.BankViewModel
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateProfileScreen(
    viewModel: BankViewModel,
    navController: NavController
) {
    var name by remember { mutableStateOf("") }
    var initialBalance by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf(1) } // 1 = Male, 2 = Female, 0 = Other
    var expanded by remember { mutableStateOf(false) }

    val genderOptions = listOf(
        1 to "Male",
        2 to "Female",
        0 to "Other"
    )

    // Handle success navigation
    LaunchedEffect(viewModel.successMessage) {
        if (viewModel.successMessage?.contains("Account created") == true) {
            navController.navigate(AppDestinations.HOMEPAGE) {
                popUpTo(AppDestinations.CREATEPROFILE) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C3E50)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate(AppDestinations.lOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Welcome Text
            Column {
                Text(
                    text = "Welcome to Yalla Banking!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = "Let's set up your banking profile",
                    fontSize = 16.sp,
                    color = Color(0xFF666666)
                )
            }

            // Name Field
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                placeholder = { Text("Enter your full name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2C3E50),
                    focusedLabelColor = Color(0xFF2C3E50)
                )
            )

            // Gender Selection
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = genderOptions.find { it.first == selectedGender }?.second ?: "",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Gender") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2C3E50),
                        focusedLabelColor = Color(0xFF2C3E50)
                    )
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genderOptions.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                selectedGender = value
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Initial Balance Field
            OutlinedTextField(
                value = initialBalance,
                onValueChange = { initialBalance = it },
                label = { Text("Initial Balance") },
                placeholder = { Text("100.00") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                leadingIcon = {
                    Text(
                        text = "$",
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2C3E50),
                    focusedLabelColor = Color(0xFF2C3E50)
                )
            )

            // Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "💡 Getting Started",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1976D2)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "• Your initial balance will be added to your account\n• You can start transferring money and joining groups immediately\n• Minimum initial balance is $10.00",
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                }
            }

            // Error/Success Messages
            viewModel.errorMessage?.let { error ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = error,
                        color = Color(0xFFD32F2F),
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Create Account Button
            Button(
                onClick = {
                    try {
                        val balance = BigDecimal(initialBalance.ifEmpty { "0" })
                        if (balance < BigDecimal("10.00")) {
                            viewModel.errorMessage = "Minimum initial balance is $10.00"
                            return@Button
                        }

                        val accountInfo = accountInformationDTO(
                            name = name.trim(),
                            balance = balance,
                            gender = selectedGender
                        )
                        viewModel.createAccount(accountInfo)
                    } catch (e: Exception) {
                        viewModel.errorMessage = "Please enter a valid balance amount"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = name.isNotBlank() && initialBalance.isNotBlank() && !viewModel.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C3E50)
                )
            ) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                } else {
                    Text(
                        text = "Create Account",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}