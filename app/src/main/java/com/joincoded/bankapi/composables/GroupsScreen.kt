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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.GroupDetailsDTO
import com.joincoded.bankapi.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    viewModel: BankViewModel,
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(1) } // Groups tab selected

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Groups",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C3E50)
                    )
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(AppDestinations.CREATEGROUP) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Create Group",
                            tint = Color(0xFF2C3E50)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            GroupsBottomNavigationBar(
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
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AppDestinations.CREATEGROUP) },
                containerColor = Color(0xFF2C3E50),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Create Group"
                )
            }
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

            // My Groups Section
            item {
                Text(
                    text = "My Groups",
                    fontSize = 18.sp,
                    color = Color(0xFF2C3E50),
                    fontWeight = FontWeight.Medium
                )
            }

            if (viewModel.userGroups.isEmpty()) {
                item {
                    EmptyGroupsCard(
                        onCreateGroupClick = { navController.navigate(AppDestinations.CREATEGROUP) }
                    )
                }
            } else {
                items(viewModel.userGroups) { group ->
                    GroupCard(
                        group = group,
                        currentUserId = viewModel.currentUserId,
                        onClick = {
                            navController.navigate("groupDetail/${group.groupId}")
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun EmptyGroupsCard(
    onCreateGroupClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCreateGroupClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Outlined.Group,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "No Groups Yet",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Create or join a group to start sharing expenses with friends and family",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCreateGroupClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C3E50)
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Create Group")
            }
        }
    }
}

@Composable
fun GroupCard(
    group: GroupDetailsDTO,
    currentUserId: Long?,
    onClick: () -> Unit
) {
    val isAdmin = group.members.find { it.userId == currentUserId }?.isAdmin == true

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF2C3E50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = group.groupName.take(2).uppercase(),
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = group.groupName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2C3E50)
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isAdmin) {
                                Text(
                                    text = "ADMIN",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            Color(0xFFFF9800),
                                            RoundedCornerShape(4.dp)
                                        )
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                            }

                            Text(
                                text = "${group.members.size} members",
                                fontSize = 12.sp,
                                color = Color(0xFF666666)
                            )
                        }
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "$${group.balance.setScale(2, java.math.RoundingMode.HALF_UP)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "Balance",
                        fontSize = 12.sp,
                        color = Color(0xFF666666)
                    )
                }
            }
        }
    }
}

@Composable
fun GroupsBottomNavigationBar(
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