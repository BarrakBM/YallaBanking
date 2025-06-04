package com.joincoded.bankapi.composables

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
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.GroupDetailsDTO
import com.joincoded.bankapi.dto.MemberDTO
import com.joincoded.bankapi.dto.userDTO
import com.joincoded.bankapi.viewmodel.BankViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailsScreen(
    groupId: Long,
    viewModel: BankViewModel = viewModel(),
    navController: NavController
) {
    var groupDetails by remember { mutableStateOf<GroupDetailsDTO?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val selectedMembers = remember { mutableStateOf<userDTO?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var showAddMemberSection by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.allUser()
    }
    LaunchedEffect(groupId) {
        coroutineScope.launch {
            try {
                isLoading = true
                errorMessage = null
                groupDetails = viewModel.getGroupDetails(groupId)
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .background(Color(0xFFF8F9FA))
            .fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = groupDetails?.groupName ?: "Group Details",
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
                    groupDetails?.let { group ->
                        val isAdmin = group.members.find { it.userId == viewModel.currentUserId }?.isAdmin == true
                        if (isAdmin) {
                            IconButton(
                                onClick = {
                                    showAddMemberSection = true

                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = "Add Member",
                                    tint = Color(0xFF2C3E50)
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFF2C3E50)
                    )
                }
            }
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Color(0xFFE74C3C),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Error: $errorMessage",
                            color = Color(0xFFE74C3C),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { navController.navigateUp() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2C3E50)
                            )
                        ) {
                            Text("Go Back")
                        }
                    }
                }
            }
            groupDetails != null -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    item {
                        GroupBalanceCard(
                            groupDetails = groupDetails!!,
                            currentUserId = viewModel.currentUserId,
                            onFundGroup = {
                                viewModel.clearMessages()
                                viewModel.selectedGroup = groupDetails
                                navController.navigate(AppDestinations.FUNDGROUP)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        GroupQuickActions(
                            groupDetails = groupDetails!!,
                            currentUserId = viewModel.currentUserId,
                            onFundGroup = {
                                viewModel.clearMessages()
                                viewModel.selectedGroup = groupDetails
                                navController.navigate(AppDestinations.FUNDGROUP)
                            },
                            onAddMember = {
                                showAddMemberSection = true
                                          },
                            onPayment = { /* Handle payment */ },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    item {
                        if (showAddMemberSection)
                        {val userFromViewModel = viewModel.userList
                            SearchContactsSection(
                                searchQuery = searchQuery,
                                onSearchChange = { searchQuery = it },
                                contacts = userFromViewModel,
                                onContactSelect = {
                                     selectedUser ->
                                        selectedMembers.value = selectedUser
                                        viewModel.addGroupMember(groupId, selectedMembers.value!!)
                                    }


                            )}
                    }

                    item {
                        Text(
                            text = "Members (${groupDetails!!.members.size})",
                            fontSize = 18.sp,
                            color = Color(0xFF2C3E50),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    items(groupDetails!!.members) { member ->
                        MemberItem(
                            member = member,
                            groupDetails = groupDetails!!,
                            currentUserId = viewModel.currentUserId,
                            onRemoveMember = { /* Handle remove member */ },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    item {
                        GroupStatisticsCard(
                            groupDetails = groupDetails!!,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupBalanceCard(
    groupDetails: GroupDetailsDTO,
    currentUserId: Long?,
    onFundGroup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAdmin = groupDetails.members.find { it.userId == currentUserId }?.isAdmin == true

    Card(
        modifier = modifier.height(160.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF34495E)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Text(
                text = groupDetails.groupName,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.align(Alignment.TopStart)
            )

            if (isAdmin) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .background(
                            Color(0xFFFF9800),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "ADMIN",
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Group Balance",
                    color = Color(0xFFCCCCCC),
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = groupDetails.balance.setScale(2, java.math.RoundingMode.HALF_UP).toString(),
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "KWD",
                        color = Color(0xFFCCCCCC),
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun GroupQuickActions(
    groupDetails: GroupDetailsDTO,
    currentUserId: Long?,
    onFundGroup: () -> Unit,
    onAddMember: () -> Unit,
    onPayment: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isAdmin = groupDetails.members.find { it.userId == currentUserId }?.isAdmin == true

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        QuickActionButton(
            icon = Icons.Outlined.AttachMoney,
            title = "Fund Group",
            onClick = onFundGroup
        )

        if (isAdmin) {
            QuickActionButton(
                icon = Icons.Outlined.PersonAdd,
                title = "Add Member",
                onClick = onAddMember
            )

            QuickActionButton(
                icon = Icons.Outlined.Payment,
                title = "Make Payment",
                onClick = onPayment
            )
        } else {
            QuickActionButton(
                icon = Icons.Outlined.Receipt,
                title = "Transactions",
                onClick = { /* Handle view transactions */ }
            )
        }
    }
}

@Composable
fun MemberItem(
    member: MemberDTO,
    groupDetails: GroupDetailsDTO,
    currentUserId: Long?,
    onRemoveMember: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    val isCurrentUser = member.userId == currentUserId
    val isCurrentUserAdmin = groupDetails.members.find { it.userId == currentUserId }?.isAdmin == true

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (member.isAdmin) Color(0xFFFF9800) else Color(0xFF2C3E50)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (member.isAdmin) Icons.Default.AdminPanelSettings else Icons.Default.Person,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = member.userName,
                        fontSize = 16.sp,
                        color = Color(0xFF2C3E50),
                        fontWeight = FontWeight.Medium
                    )
                    if (isCurrentUser) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "(You)",
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (member.isAdmin) "Group Admin" else "Member",
                    fontSize = 12.sp,
                    color = if (member.isAdmin) Color(0xFFFF9800) else Color(0xFF666666)
                )
            }

            if (isCurrentUserAdmin && !member.isAdmin && !isCurrentUser) {
                IconButton(
                    onClick = { onRemoveMember(member.userId) }
                ) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle,
                        contentDescription = "Remove Member",
                        tint = Color(0xFFE74C3C)
                    )
                }
            }
        }
    }
}

@Composable
fun GroupStatisticsCard(
    groupDetails: GroupDetailsDTO,
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
                text = "Group Statistics",
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticItem(
                    title = "Total Members",
                    value = groupDetails.members.size.toString(),
                    icon = Icons.Outlined.Group
                )

                StatisticItem(
                    title = "Admin",
                    value = "1",
                    icon = Icons.Outlined.AdminPanelSettings
                )

                StatisticItem(
                    title = "Balance",
                    value = "${groupDetails.balance.setScale(2)} KWD",
                    icon = Icons.Outlined.AccountBalance
                )
            }
        }
    }
}

@Composable
fun StatisticItem(
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
            color = Color(0xFF666666),
            textAlign = TextAlign.Center
        )
    }
}



//------------------------

