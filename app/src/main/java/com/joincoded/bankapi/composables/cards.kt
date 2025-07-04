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

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.GroupDetailsDTO
import com.joincoded.bankapi.viewmodel.BankViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    viewModel: BankViewModel = viewModel(),
    navController: NavController,
    // navigation to card details when view details is clicked
    onNavigateToCardDetails: () -> Unit = {}
) {

    var selectedTab by remember { mutableStateOf(2) } // cards tab selected

    LaunchedEffect(Unit) {
        viewModel.loadUserAccount()  // Fetch account info from backend
        viewModel.loadUserGroups()   // Fetch user groups from backend
    }

    Scaffold(
        modifier = Modifier
            .background(Color(0xFFF8F9FA))
            .fillMaxSize(), // Ensure Scaffold takes full size
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cards",
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
            CardsBottomNavigationBar(
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
                .fillMaxSize() // Ensure LazyColumn takes full available size
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // user Card Section
            item {
                CardSection(
                    sectionTitle = "YOUR CARD",
                    cardTitle = "Yalla Banking",

                    // take last four digits of user ID to show as card number
                    cardNumber = "**** **** **** ${viewModel.currentUserId?.toString()?.padStart(4, '0') ?: "0000"}",

                    cardHolder = viewModel.userAccount?.name ?: "Card Holder",
                    expiryDate = "08/29",

                    // blue card means active card, grey card means not
                    cardColor = if (viewModel.userAccount?.isActive == true) Color(0xFF2C3E50) else Color(0xFF999999),

                    cardIcon = Icons.Outlined.AccountBox,
                    onViewDetailsClick = onNavigateToCardDetails // navigate to card details
                )
            }

            // group Cards Section
            if (viewModel.userGroups.isNotEmpty()) {
                items(viewModel.userGroups) { group ->
                    GroupCardSection(
                        group = group,
                        currentUserId = viewModel.currentUserId,
                        onViewDetailsClick = {
                            // Navigate to group details
                            navController.navigate("groupDetail/${group.groupId}")
                        }
                    )
                }
            } else if (!viewModel.isLoading) {
                //  placeholder when no groups exist
                item {
                    NoGroupsCard(
                        onCreateGroupClick = {
                            navController.navigate(AppDestinations.CREATEGROUP)
                        }
                    )
                }
            }

            // loading indicator for groups
            if (viewModel.isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF2C3E50)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupCardSection(
    group: GroupDetailsDTO,
    currentUserId: Long?,
    onViewDetailsClick: () -> Unit
) {
    val isAdmin = group.members.find { it.userId == currentUserId }?.isAdmin == true

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.Group,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "GROUP CARD",
                fontSize = 12.sp,
                color = Color(0xFF999999),
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
            if (isAdmin) {
                Spacer(modifier = Modifier.width(8.dp))
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
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        DetailedBankCard(
            cardTitle = group.groupName,
            cardNumber = "**** **** **** ${group.groupId.toString().padStart(4, '0')}",
            cardHolder = "${group.members.size} members",
            expiryDate = "No Expiry",
            backgroundColor = Color(0xFF34495E),
            cardIcon = Icons.Outlined.Group
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onViewDetailsClick() }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "View Details",
                    tint = Color(0xFF2C3E50),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "View Details",
                    fontSize = 14.sp,
                    color = Color(0xFF2C3E50),
                    fontWeight = FontWeight.Medium
                )
            }

            Text(
                text = "$${group.balance.setScale(2, java.math.RoundingMode.HALF_UP)}",
                fontSize = 14.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun NoGroupsCard(
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
                text = "No Group Cards",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2C3E50)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Join or create a group to get a group card",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center
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
fun CardSection(
    sectionTitle: String,
    sectionSubtitle: String? = null,
    cardTitle: String,
    cardNumber: String,
    cardHolder: String,
    expiryDate: String,
    cardColor: Color,
    cardIcon: ImageVector,
    onViewDetailsClick: () -> Unit = {}
) {
    Column {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (sectionSubtitle != null) Icons.Outlined.AccountBox else Icons.Outlined.Person,
                contentDescription = null,
                tint = Color(0xFF999999),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = sectionTitle,
                fontSize = 12.sp,
                color = Color(0xFF999999),
                fontWeight = FontWeight.Medium,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        DetailedBankCard(
            cardTitle = cardTitle,
            cardNumber = cardNumber,
            cardHolder = cardHolder,
            expiryDate = expiryDate,
            backgroundColor = cardColor,
            cardIcon = cardIcon
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { onViewDetailsClick() }
            ) {

                Icon(
                    imageVector = Icons.Outlined.Menu,
                    contentDescription = "View Details",
                    tint = Color(0xFF2C3E50),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "View Details",
                    fontSize = 14.sp,
                    color = Color(0xFF2C3E50),
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}

@Composable
fun DetailedBankCard(
    cardTitle: String,
    cardNumber: String,
    cardHolder: String,
    expiryDate: String,
    backgroundColor: Color,
    cardIcon: ImageVector
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {

            Text(
                text = cardTitle,
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
                    text = "VISA",
                    color = Color(0xFF2C3E50),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // expiration date
            Column(
                modifier = Modifier.align(Alignment.BottomStart)
            ) {
                Text(
                    text = "Card Holder",
                    color = Color(0xFFCCCCCC),
                    fontSize = 13.sp,
                    lineHeight = 10.sp
                )
                Text(
                    text = cardHolder,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 12.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = cardNumber,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Normal,
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = "Valid thru",
                    color = Color(0xFFCCCCCC),
                    fontSize = 10.sp,
                    lineHeight = 10.sp
                )
                Text(
                    text = "05/28",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 12.sp
                )
            }
        }
    }
}

@Composable
fun CardsBottomNavigationBar(
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