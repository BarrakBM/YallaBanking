package com.joincoded.bankapi.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel
import com.joincoded.bankapi.viewmodel.BankViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardsScreen(
    viewModel: BankViewModel = viewModel(),
    // navigation to card details when view details is clicked
    onNavigateToCardDetails: () -> Unit = {}
) {

    var selectedTab by remember { mutableStateOf(1) } // cards tab selected

    LaunchedEffect(Unit) {
        viewModel.loadUserAccount()  // Fetch account info from backend
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cards",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF2C3E50)
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back navigation */ }) {
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            CardsBottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // always show real account data from backend
            item {
                CardSection(
                    sectionTitle = "YOUR CARD",           // ORIGINAL: Section title preserved
                    cardTitle = "Yalla Banking",          // ORIGINAL: Card title preserved

                    // take last for digits of balance to show as card number
                    cardNumber = "**** **** **** ${viewModel.currentUserId?.toString()?.padStart(4, '0') ?: "0000"}",

                    cardHolder = viewModel.userAccount?.name ?: "card holder",
                    expiryDate = "08/29",

                    // blue card means active card, grey card means not
                    cardColor = if (viewModel.userAccount?.isActive == true) Color(0xFF2C3E50) else Color(0xFF999999),

                    cardIcon = Icons.Outlined.AccountBox,
                    onViewDetailsClick = onNavigateToCardDetails // navigate to card details
                )
            }

            // TODO: replace with real data
            item {
                CardSection(
                    sectionTitle = "GROUP CARD",
                    sectionSubtitle = "ADMIN",
                    cardTitle = "Yalla Group Card",
                    cardNumber = "**** 8934 7654 1234",
                    cardHolder = "Team Yalla",
                    expiryDate = "12/25",
                    cardColor = Color(0xFF34495E),
                    cardIcon = Icons.Outlined.AccountBox,
                    onViewDetailsClick = onNavigateToCardDetails
                )
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
                modifier = Modifier.clickable { onViewDetailsClick() } // NEW: Made clickable
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

                    text = if (cardTitle.contains("Group")) "VISA" else "VISA",
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

                Spacer(modifier = Modifier.height(8.dp))  // Add controlled spacing before card number

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
                    lineHeight = 10.sp  // Remove extra line spacing
                )
                Text(
                    text = expiryDate,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    lineHeight = 12.sp  // Remove extra line spacing
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
    // ORIGINAL: Navigation bar container with styling
    NavigationBar(
        containerColor = Color.White,    // ORIGINAL: White background
        tonalElevation = 8.dp           // ORIGINAL: Subtle shadow elevation
    ) {

        // ORIGINAL: Home tab (index 0)
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
                    imageVector = if (selectedTab == 1) Icons.Filled.CreditCard else Icons.Outlined.CreditCard,
                    contentDescription = "Cards"
                )
            },
            label = { Text("Cards") },
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
                    imageVector = if (selectedTab == 2) Icons.Filled.Group else Icons.Outlined.Group,
                    contentDescription = "Groups"
                )
            },
            label = { Text("Groups") },
            selected = selectedTab == 2,
            onClick = { onTabSelected(2) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xFF2C3E50),
                selectedTextColor = Color(0xFF2C3E50),
                unselectedIconColor = Color(0xFF999999),
                unselectedTextColor = Color(0xFF999999)
            )
        )

        // ORIGINAL: Profile tab (index 3)
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

@Preview(showBackground = true)
@Composable
fun CardsScreenPreview() {
    MaterialTheme {
        CardsScreen()  // ORIGINAL: Shows CardsScreen with default parameters
    }
}