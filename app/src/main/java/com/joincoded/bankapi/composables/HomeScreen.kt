package com.joincoded.bankapi.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.allTransactionDTO
import com.joincoded.bankapi.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(

    viewModel: BankViewModel,
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(0) }

    // Load data when screen opens
    LaunchedEffect(Unit) {
        viewModel.loadUserAccount()
        viewModel.loadTransactionHistory()
    }

    // Show error messages
    viewModel.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // You can show a snackbar or toast here
            viewModel.clearMessages()
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        0 -> navController.navigate(AppDestinations.HOMEPAGE)
                        1 -> navController.navigate(AppDestinations.GROUPS)
                        2 -> navController.navigate(AppDestinations.CARDS)
                        3 -> navController.navigate(AppDestinations.PROFILE)
                    }
                }
            )
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Yalla Banking",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C3E50)
                    )
                },
                actions = {
                    IconButton(onClick = { /* Handle notifications */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                    IconButton(onClick = { /* Handle settings */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Balance Section
            item {
                BalanceSection(
                    balance = viewModel.userAccount?.balance ?: java.math.BigDecimal.ZERO,
                    isLoading = viewModel.isLoading
                )
            }

            // Quick Actions
            item {
                QuickActionsSection(
                    onTransferClick = { navController.navigate(AppDestinations.TRANSFER) },
                    onCardsClick = { navController.navigate(AppDestinations.CARDS) },
                    onGroupsClick = { navController.navigate(AppDestinations.GROUPS) }
                )
            }

            // Cards Section
            item {
                viewModel.currentUserId?.let {
                    CardsSection(
                        userAccount = viewModel.userAccount,
                        onSeeAllClick = { navController.navigate(AppDestinations.CARDS) },
                        userId= it
                    )
                }
            }

            // Recent Transactions
            item {
                RecentTransactionsSection(
                    transactions = viewModel.transactionHistory.take(5),
                    isLoading = viewModel.isLoading,
                    onSeeAllClick = { /* Navigate to full transaction history */ }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun BalanceSection(
    balance: java.math.BigDecimal,
    isLoading: Boolean
) {
    Column {
        Text(
            text = "Total Balance",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color(0xFF2C3E50)
            )
        } else {
            Text(
                text = "${balance.setScale(2, java.math.RoundingMode.HALF_UP)}",
                fontSize = 32.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun QuickActionsSection(
    onTransferClick: () -> Unit,
    onCardsClick: () -> Unit,
    onGroupsClick: () -> Unit
) {
    Column {
        Text(
            text = "Quick Actions",
            fontSize = 18.sp,
            color = Color(0xFF2C3E50),
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickActionItem(
                icon = Icons.Outlined.Send,
                title = "Transfer",
                onClick = onTransferClick
            )
            QuickActionItem(
                icon = Icons.Outlined.CreditCard,
                title = "Cards",
                onClick = onCardsClick
            )
            QuickActionItem(
                icon = Icons.Outlined.Group,
                title = "Groups",
                onClick = onGroupsClick
            )
        }
    }
}

@Composable
fun CardsSection(
    userId:Long,
    userAccount: com.joincoded.bankapi.dto.InformationDTO?,
    onSeeAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Cards",
                fontSize = 18.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Main Card
        BankCard(
            cardType = userAccount?.name ?: "Your Card",
            cardNumber = "**** **** **** ${userId.toString().padStart(4, '0') ?: "0000"}",
            validThru = "08/29",
            cardBrand = "VISA",
            backgroundColor = if (userAccount?.isActive == true) Color(0xFF2C3E50) else Color(0xFF999999)
        )
    }
}

@Composable
fun RecentTransactionsSection(
    transactions: List<allTransactionDTO>,
    isLoading: Boolean,
    onSeeAllClick: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Recent Transactions",
                fontSize = 18.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )
            TextButton(onClick = onSeeAllClick) {
                Text(
                    text = "See all",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color(0xFF2C3E50)
            )
        } else if (transactions.isEmpty()) {
            Text(
                text = "No transactions yet",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else {
            transactions.forEach { transaction ->
                TransactionItem(transaction = transaction)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun TransactionItem(transaction: allTransactionDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = transaction.from ?: "Unknown",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = transaction.time?.toString() ?: "",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }

            Text(
                text = "${if (transaction.amount >= java.math.BigDecimal.ZERO) "+" else ""}${transaction.amount.setScale(2, java.math.RoundingMode.HALF_UP)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (transaction.amount >= java.math.BigDecimal.ZERO) Color(0xFF27AE60) else Color(0xFFE74C3C)
            )
        }
    }
}

@Composable
fun BankCard(
    cardType: String,
    cardNumber: String,
    validThru: String,
    cardBrand: String,
    backgroundColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = cardType,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.TopStart)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(40.dp, 24.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = cardBrand,
                    color = Color(0xFF2C3E50),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = cardNumber,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.CenterStart)
            )

            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = "Valid thru",
                    color = Color(0xFFCCCCCC),
                    fontSize = 10.sp
                )
                Text(
                    text = validThru,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun QuickActionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF666666),
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

@Composable
fun BottomNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedTab == 0) Icons.Filled.Home else Icons.Outlined.Home,
                    contentDescription = "Home"
                )
            },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { onTabSelected(0) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2C3E50),
                selectedTextColor = Color(0xFF2C3E50),
                unselectedIconColor = Color(0xFF999999),
                unselectedTextColor = Color(0xFF999999)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedTab == 1) Icons.Filled.Group else Icons.Outlined.Group,
                    contentDescription = "Groups"
                )
            },
            label = { Text("Groups") },
            selected = selectedTab == 1,
            onClick = { onTabSelected(1) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2C3E50),
                selectedTextColor = Color(0xFF2C3E50),
                unselectedIconColor = Color(0xFF999999),
                unselectedTextColor = Color(0xFF999999)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedTab == 2) Icons.Filled.CreditCard else Icons.Outlined.CreditCard,
                    contentDescription = "Cards"
                )
            },
            label = { Text("Cards") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2C3E50),
                selectedTextColor = Color(0xFF2C3E50),
                unselectedIconColor = Color(0xFF999999),
                unselectedTextColor = Color(0xFF999999)
            )
        )

        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = if (selectedTab == 3) Icons.Filled.Person else Icons.Outlined.Person,
                    contentDescription = "Profile"
                )
            },
            label = { Text("Profile") },
            selected = selectedTab == 3,
            onClick = { onTabSelected(3) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2C3E50),
                selectedTextColor = Color(0xFF2C3E50),
                unselectedIconColor = Color(0xFF999999),
                unselectedTextColor = Color(0xFF999999)
            )
        )
    }
}