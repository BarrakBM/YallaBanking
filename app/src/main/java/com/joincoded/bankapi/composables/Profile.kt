package com.joincoded.bankapi.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: BankViewModel,
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(3) } // Profile tab selected
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showDeactivateDialog by remember { mutableStateOf(false) }

    // Handle logout navigation
    LaunchedEffect(viewModel.isLoggedIn) {
        if (!viewModel.isLoggedIn) {
            navController.navigate(AppDestinations.lOGIN) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C3E50)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            ProfileBottomNavigationBar(
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
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Profile Header
            item {
                ProfileHeader(
                    name = viewModel.userAccount?.name ?: "User",
                    userId = viewModel.currentUserId ?: 0L,
                    isActive = viewModel.userAccount?.isActive ?: false
                )
            }

            // Account Info Section
            item {
                AccountInfoSection(
                    balance = viewModel.userAccount?.balance?.setScale(2, java.math.RoundingMode.HALF_UP)?.toString() ?: "0.00",
                    accountStatus = if (viewModel.userAccount?.isActive == true) "Active" else "Inactive"
                )
            }

            // Menu Options
            item {
                MenuSection(
                    onCardsClick = { navController.navigate(AppDestinations.CARDS) },
                    onTransferClick = { navController.navigate(AppDestinations.TRANSFER) },
                    onGroupsClick = { navController.navigate(AppDestinations.GROUPS) },
                    onDeactivateClick = { showDeactivateDialog = true },
                    onLogoutClick = { showLogoutDialog = true }
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Logout Confirmation Dialog
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("Logout") },
                text = { Text("Are you sure you want to logout?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            viewModel.logout()
                        }
                    ) {
                        Text("Logout", color = Color(0xFF2C3E50))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showLogoutDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        // Deactivate Account Dialog
        if (showDeactivateDialog) {
            AlertDialog(
                onDismissRequest = { showDeactivateDialog = false },
                title = { Text("Deactivate Account") },
                text = { Text("Are you sure you want to deactivate your account? This action cannot be undone.") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeactivateDialog = false
                            viewModel.deactivateAccount()
                        }
                    ) {
                        Text("Deactivate", color = Color(0xFFE74C3C))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDeactivateDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    userId: Long,
    isActive: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Profile Avatar
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C3E50)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = name.take(2).uppercase(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = "ID: $userId",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(
                                if (isActive) Color(0xFF4CAF50) else Color(0xFFF44336)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isActive) "Active" else "Inactive",
                        fontSize = 12.sp,
                        color = if (isActive) Color(0xFF4CAF50) else Color(0xFFF44336),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun AccountInfoSection(
    balance: String,
    accountStatus: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Account Information",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Current Balance",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Text(
                    text = "$$balance",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C3E50)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Account Status",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
                Text(
                    text = accountStatus,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF2C3E50)
                )
            }
        }
    }
}

@Composable
fun MenuSection(
    onCardsClick: () -> Unit,
    onTransferClick: () -> Unit,
    onGroupsClick: () -> Unit,
    onDeactivateClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            MenuOption(
                icon = Icons.Outlined.CreditCard,
                title = "My Cards",
                onClick = onCardsClick
            )

            MenuOption(
                icon = Icons.Outlined.Send,
                title = "Transfer Money",
                onClick = onTransferClick
            )

            MenuOption(
                icon = Icons.Outlined.Group,
                title = "My Groups",
                onClick = onGroupsClick
            )

            Divider(modifier = Modifier.padding(horizontal = 16.dp))

            MenuOption(
                icon = Icons.Outlined.Close,
                title = "Deactivate Account",
                onClick = onDeactivateClick,
                textColor = Color(0xFFE74C3C)
            )

            MenuOption(
                icon = Icons.Outlined.ExitToApp,
                title = "Logout",
                onClick = onLogoutClick,
                textColor = Color(0xFFE74C3C)
            )
        }
    }
}

@Composable
fun MenuOption(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    textColor: Color = Color(0xFF2C3E50)
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = textColor,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = Color(0xFF999999),
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun ProfileBottomNavigationBar(
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