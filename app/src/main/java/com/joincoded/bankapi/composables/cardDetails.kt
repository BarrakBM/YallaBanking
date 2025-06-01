package com.joincoded.bankapi.composables

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.joincoded.bankapi.composables.CardsBottomNavigationBar
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.joincoded.bankapi.viewmodel.BankViewModel
import com.joincoded.bankapi.dto.allTransactionDTO
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardDetailsScreen(
    viewModel: BankViewModel = viewModel(),
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(1) } // Cards tab selected
    val coroutineScope = rememberCoroutineScope()

    // Load transaction history when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadTransactionHistory()
    }

    // Use Box with fillMaxSize to ensure full screen coverage
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(), // Ensure Scaffold takes full size
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Card Details",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2C3E50),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color(0xFF2C3E50)
                            )
                        }
                    },
                    actions = {
                        // Add empty action to balance the layout with navigation icon
                        IconButton(onClick = { }, enabled = false) {
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White
                    )
                )
            },
            bottomBar = {
                CardsBottomNavigationBar(
                    selectedTab = selectedTab,
                    onTabSelected = { selectedTab = it },
                )
            },
            containerColor = Color(0xFFF8F9FA)
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize() // Ensure LazyColumn takes full available size
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
            ) {
                // Card Balance Section
                item {
                    CardBalanceSection(
                        viewModel = viewModel,
                        modifier = Modifier.fillMaxWidth() // Ensure full width
                    )
                }

                // Quick Actions Section
                item {
                    QuickActionsSection(
                        viewModel = viewModel,
                        navController = navController,
                        onDeactivate = {
                            coroutineScope.launch {
                                viewModel.deactivateAccount()
                            }
                        },
                        modifier = Modifier.fillMaxWidth() // Ensure full width
                    )
                }

                // Recent Transactions Section
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Text(
                            text = "Recent Transactions",
                            fontSize = 18.sp,
                            color = Color(0xFF2C3E50),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Transaction List
                if (viewModel.transactionHistory.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "No transactions yet",
                                color = Color(0xFF999999),
                                fontSize = 14.sp
                            )
                            // Add debug info to see loading state
                            if (viewModel.isLoading) {
                                Text(
                                    text = "Loading transactions...",
                                    color = Color(0xFF666666),
                                    fontSize = 12.sp
                                )
                            }
                            // Show error if any
                            viewModel.errorMessage?.let { error ->
                                Text(
                                    text = "Error: $error",
                                    color = Color(0xFFFF6B6B),
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                } else {
                    items(viewModel.transactionHistory) { transaction ->
                        TransactionItem(
                            transaction = transaction,
                            userAccountName = viewModel.userAccount?.name,
                            modifier = Modifier.fillMaxWidth() // Ensure full width
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CardBalanceSection(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val userAccount = viewModel.userAccount
    val balance = userAccount?.balance ?: 0.0
    val isActive = userAccount?.isActive ?: false

    Card(
        modifier = modifier
            .height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color(0xFF2C3E50) else Color(0xFF999999)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            // Balance Label
            Text(
                text = if (isActive) "Balance" else "DEACTIVATED",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.TopStart)
            )

            // Card Brand
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp, 24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "VISA",
                    color = if (isActive) Color(0xFF2C3E50) else Color(0xFF999999),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Balance Amount
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = String.format("%.2f", balance),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "AED",
                    color = Color(0xFFCCCCCC),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Card Number and Expiry
            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = "**** ${viewModel.currentUserId?.toString()?.padStart(4, '0') ?: "0000"}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
                Text(
                    text = "Exp 08/29",
                    color = Color(0xFFCCCCCC),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun QuickActionsSection(
    viewModel: BankViewModel,
    navController: NavController,
    onDeactivate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionButton(
            icon = Icons.Outlined.Send,
            title = "Send",
            onClick = { navController.navigate("transfer") }
        )
        QuickActionButton(
            icon = Icons.Outlined.ArrowDownward,
            title = "Deposit",
            onClick = { /* Handle deposit */ }
        )
        QuickActionButton(
            icon = Icons.Outlined.Block,
            title = if (viewModel.userAccount?.isActive == true) "Deactivate" else "Activate",
            onClick = onDeactivate
        )
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .size(56.dp)
                .background(
                    color = Color.White,
                    shape = CircleShape
                )
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF2C3E50),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TransactionItem(
    transaction: allTransactionDTO,
    userAccountName: String?,
    modifier: Modifier = Modifier
) {
    val isOutgoing = transaction.from.equals(userAccountName, ignoreCase = true)
    val icon = when (transaction.type) {
        "user" -> if (isOutgoing) Icons.Default.Send else Icons.Default.Person
        "group" -> Icons.Default.Group
        else -> Icons.Default.AttachMoney
    }

    val backgroundColor = when (transaction.type) {
        "group" -> Color(0xFF34495E)
        else -> Color(0xFFF0F0F0)
    }

    Row(
        modifier = modifier
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Transaction Icon
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (backgroundColor == Color(0xFF34495E)) Color.White else Color(0xFF666666),
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Transaction Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = when (transaction.type) {
                    "group" -> "Fund to ${transaction.to}"
                    else -> if (isOutgoing) {
                        "Transfer to ${transaction.to}"
                    } else {
                        "Received from ${transaction.from}"
                    }
                },
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${transaction.type.uppercase()} • ${transaction.time}",
                fontSize = 12.sp,
                color = Color(0xFF666666),
                lineHeight = 16.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Amount
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "${if (isOutgoing) "-" else "+"}${
                    String.format(
                        "%.2f",
                        transaction.amount
                    )
                }",
                fontSize = 16.sp,
                color = if (isOutgoing) Color(0xFF2C3E50) else Color(0xFF4CAF50),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "AED",
                fontSize = 12.sp,
                color = Color(0xFF666666)
            )
        }
    }
}