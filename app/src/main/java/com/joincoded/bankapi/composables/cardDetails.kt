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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var selectedTab by remember { mutableStateOf(1) } // cards tab selected
    val coroutineScope = rememberCoroutineScope()

    // load transaction history when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadTransactionHistory()
        viewModel.loadUserGroups() // Also load groups for complete transaction context
    }

    Scaffold(
        modifier = Modifier
            .background(Color(0xFFF8F9FA))
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                modifier = Modifier,
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
                    IconButton(onClick = { }, enabled = false) {
                    }
                },
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Card Balance Section
            item {
                CardBalanceSection(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Account Summary Section
            item {
                AccountSummarySection(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Recent transactions header
            item {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = "All Transactions",
                        fontSize = 18.sp,
                        color = Color(0xFF2C3E50),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Transaction List with enhanced display
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
                        if (viewModel.isLoading) {
                            Text(
                                text = "Loading transactions...",
                                color = Color(0xFF666666),
                                fontSize = 12.sp
                            )
                        }
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
                    EnhancedTransactionItem(
                        transaction = transaction,
                        userAccountName = viewModel.userAccount?.name,
                        userGroups = viewModel.userGroups,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun CardBalanceSection(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val userAccount = viewModel.userAccount
    val balance = userAccount?.balance ?: java.math.BigDecimal.ZERO
    val isActive = userAccount?.isActive ?: false

    Card(
        modifier = modifier.height(140.dp),
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
            // Balance
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
                    text = balance.setScale(2, java.math.RoundingMode.HALF_UP).toString(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "KWD",
                    color = Color(0xFFCCCCCC),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // card Number and expiry
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
fun AccountSummarySection(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Account Summary",
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem(
                    title = "Total Groups",
                    value = viewModel.userGroups.size.toString(),
                    icon = Icons.Outlined.Group
                )
                SummaryItem(
                    title = "Transactions",
                    value = viewModel.transactionHistory.size.toString(),
                    icon = Icons.Outlined.Receipt
                )
                SummaryItem(
                    title = "Status",
                    value = if (viewModel.userAccount?.isActive == true) "Active" else "Inactive",
                    icon = if (viewModel.userAccount?.isActive == true) Icons.Outlined.CheckCircle else Icons.Outlined.Block
                )
            }
        }
    }
}

@Composable
fun SummaryItem(
    title: String,
    value: String,
    icon: ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF2C3E50),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color(0xFF666666)
        )
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
            icon = Icons.Outlined.Group,
            title = "Groups",
            onClick = { navController.navigate("groups") }
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
fun EnhancedTransactionItem(
    transaction: allTransactionDTO,
    userAccountName: String?,
    userGroups: List<com.joincoded.bankapi.dto.GroupDetailsDTO>,
    modifier: Modifier = Modifier
) {
    val isOutgoing = transaction.from.equals(userAccountName, ignoreCase = true)

    // get the names
    val (transactionIcon, backgroundColor, transactionTitle) = when (transaction.type.lowercase()) {
        "group" -> {
            val groupName = userGroups.find { it.groupName.equals(transaction.to, ignoreCase = true) }?.groupName
                ?: transaction.to
            Triple(
                Icons.Default.Group,
                Color(0xFF34495E),
                if (isOutgoing) "Fund to $groupName" else "Received from $groupName"
            )
        }
        "user" -> {

            Triple(
                if (isOutgoing) Icons.Default.Send else Icons.Default.Person,
                Color(0xFFF0F0F0),
                if (isOutgoing) "Transfer to ${transaction.to}" else "Received from ${transaction.from}"
            )
        }
        else -> Triple(
            Icons.Default.AttachMoney,
            Color(0xFFF0F0F0),
            "Transaction"
        )
    }

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // transaction Icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = transactionIcon,
                    contentDescription = null,
                    tint = if (backgroundColor == Color(0xFF34495E)) Color.White else Color(0xFF666666),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            //  details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transactionTitle,
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

            // amount
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "${if (isOutgoing) "-" else "+"}${
                        transaction.amount.setScale(2, java.math.RoundingMode.HALF_UP)
                    }",
                    fontSize = 16.sp,
                    color = if (isOutgoing) Color(0xFFE74C3C) else Color(0xFF27AE60),
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "KWD",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}