package com.joincoded.bankapi.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.joincoded.bankapi.viewmodel.BankViewModel
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.GroupDetailsDTO
import java.math.BigDecimal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundGroupScreen(viewModel: BankViewModel, navController: NavController,group: GroupDetailsDTO) {
    var selectedTab by remember { mutableStateOf(1) } // Groups tab selected
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Load user account data
    LaunchedEffect(Unit) {
        viewModel.loadUserAccount()
    }
    LaunchedEffect(Unit) {
        viewModel.getGroupDetails(group.groupId)
    }

    val success = viewModel.successMessage
    LaunchedEffect(success) {
        if (success != null) {
            navController.popBackStack() // Or navigate to a success screen
        }
    }




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Fund Group",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C3E50)
                    )
                },
                navigationIcon = {
                    /* Handle back navigation */
                    IconButton(onClick = { navController.popBackStack() }) {
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
        bottomBar = {
            FundGroupBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        0 -> navController.navigate(AppDestinations.HOMEPAGE)
                        1 -> navController.navigate(AppDestinations.GROUPS)
                        2 -> navController.navigate(AppDestinations.TRANSFER)
                        3 -> navController.navigate(AppDestinations.PROFILE)
                    }
                }
            )

        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Group Info Section
            GroupInfoCard(group= group)

            // Amount Section
            AmountInputSection(
                amount = amount,
                onAmountChange = { amount = it }
            )

            // Description Section
            DescriptionInputSection(
                description = description,
                onDescriptionChange = { description = it }
            )

            Spacer(modifier = Modifier.weight(1f))
            ////////////////////////////////////////////////////////////////////////fatma check again
            // Fund Group Button
            Button(
                onClick = {
                    val parsedAmount = amount.toBigDecimalOrNull()
                    if (parsedAmount != null && parsedAmount > BigDecimal.ZERO) {
                        viewModel.fundGroup(
                            groupId = group.groupId,
                            amount = parsedAmount,
                            description = if (description.isNotBlank()) description else "Group funding"
                        )
                    }
                    navController.navigate(AppDestinations.GROUPDETAIL)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2C3E50)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = "Fund",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fund Group",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun GroupInfoCard(group: GroupDetailsDTO) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Group Avatar
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C3E50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = "Group",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))
///////////////////////////////////////////
        // Group Details
        Column {
            Text(
                text = group.groupName,
                fontSize = 18.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "${group.members.size} members",
                fontSize = 14.sp,
                color = Color(0xFF666666)
            )
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//                Icon(
//                    imageVector = Icons.Default.Group,
//                    contentDescription = "Members",
//                    tint = Color(0xFF666666),
//                    modifier = Modifier.size(14.dp)
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = "5 members",
//                    fontSize = 14.sp,
//                    color = Color(0xFF666666)
//                )
            }
        }
    }


@Composable
fun AmountInputSection(
    amount: String,
    onAmountChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Amount",
            fontSize = 16.sp,
            color = Color(0xFF2C3E50),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "KD",
                    fontSize = 20.sp,
                    color = Color(0xFF666666),
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = amount,
                    onValueChange = onAmountChange,
                    placeholder = {
                        Text(
                            text = "0.00",
                            fontSize = 20.sp,
                            color = Color(0xFFCCCCCC)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 20.sp,
                        color = Color(0xFF2C3E50),
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}

@Composable
fun DescriptionInputSection(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Description",
            fontSize = 16.sp,
            color = Color(0xFF2C3E50),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                placeholder = {
                    Text(
                        text = "e.g. For dinner expenses",
                        color = Color(0xFFCCCCCC),
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 16.sp,
                    color = Color(0xFF2C3E50)
                ),
                maxLines = 4
            )
        }
    }
}

@Composable
fun FundGroupBottomNav(
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
                    imageVector = if (selectedTab == 2) Icons.Filled.SwapHoriz else Icons.Outlined.SwapHoriz,
                    contentDescription = "Transfer"
                )
            },
            label = { Text("Transfer") },
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

//@Preview(showBackground = true)
//@Composable
//fun FundGroupScreenPreview() {
//    MaterialTheme {
//        FundGroupScreen()
//    }
//}