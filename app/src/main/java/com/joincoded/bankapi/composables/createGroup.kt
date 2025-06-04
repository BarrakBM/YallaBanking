package com.joincoded.bankapi.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.joincoded.bankapi.AppNavigator.AppDestinations
import com.joincoded.bankapi.dto.CreateGroupRequest
import com.joincoded.bankapi.dto.userDTO
import com.joincoded.bankapi.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGroupScreen(
    bankViewModel: BankViewModel,
    navController: NavController
) {
    var selectedTab by remember { mutableStateOf(2) } // Groups tab selected
    var groupName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var searchQuery by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    val selectedMembers = remember { mutableStateListOf<userDTO>() }

    val allContacts = remember { mutableStateListOf<userDTO>() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        bankViewModel.allUser()
    }

    // Watch for success message changes
    LaunchedEffect(bankViewModel.successMessage) {
        if (bankViewModel.successMessage?.contains("T") == true) {
            showConfirmDialog = true
        }
    }

    if (showConfirmDialog) {
        GroupCreatedDialog(
            onDismiss = {
                showConfirmDialog = false
                bankViewModel.clearMessages()
            },
            onConfirm = {
                showConfirmDialog = false
                bankViewModel.clearMessages()
                navController.navigate(AppDestinations.GROUPS) {
                    popUpTo(AppDestinations.GROUPS) { inclusive = true }
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Create Group",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            CreateGroupBottomNav(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    selectedTab = tab
                    when (tab) {
                        0 -> navController.navigate(AppDestinations.HOMEPAGE)
                        1 -> navController.navigate(AppDestinations.CARDS)
                        2 -> navController.navigate(AppDestinations.GROUPS)
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
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Group Icon Section
            item {
                GroupIconSection()
            }

            // Group Name Section
            item {
                GroupNameSection(
                    groupName = groupName,
                    onGroupNameChange = { groupName = it }
                )
            }

            // Add Members Section
            item {
                AddMembersSection(
                    selectedMembers = selectedMembers,
                    onRemoveMember = { member ->
                        selectedMembers.remove(member)
                    }
                )
            }

            // Search Contacts Section
            item {
                val userFromViewModel = bankViewModel.userList
                SearchContactsSection(
                    searchQuery = searchQuery,
                    onSearchChange = { searchQuery = it },
                    contacts = userFromViewModel,
                    onContactSelect = { contact ->
                        if (!selectedMembers.any { it.userId == contact.userId }) {
                            selectedMembers.add(contact)
                        }
                    }
                )
            }

            // Create Group Button
            item {
                Button(
                    onClick = {
                        if (groupName.isNotBlank()) {
                            bankViewModel.createGroup(CreateGroupRequest(
                                name = groupName,
                                description = description,
                                memberIds = selectedMembers.map { it.userId.toString() }
                            ))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C3E50)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    enabled = groupName.isNotBlank() && !bankViewModel.isLoading
                ) {
                    if (bankViewModel.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = Color.White
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Create",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Create Group",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GroupIconSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Group Icon Circle
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .clickable { /* Handle change icon */ },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Group,
                contentDescription = "Group Icon",
                tint = Color.White,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Change Icon Button
        TextButton(
            onClick = { /* Handle change icon */ }
        ) {
            Text(
                text = "Change Icon",
                fontSize = 14.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun GroupNameSection(
    groupName: String,
    onGroupNameChange: (String) -> Unit
) {
    Column {
        Text(
            text = "Group Name",
            fontSize = 16.sp,
            color = Color(0xFF2C3E50),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = groupName,
            onValueChange = onGroupNameChange,
            placeholder = {
                Text(
                    text = "Enter group name",
                    color = Color(0xFFCCCCCC)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2C3E50),
                unfocusedBorderColor = Color(0xFFDDDDDD),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )
    }
}

@Composable
fun AddMembersSection(
    selectedMembers: List<userDTO>,
    onRemoveMember: (userDTO) -> Unit
) {
    Column {
        Text(
            text = "Add Members",
            fontSize = 16.sp,
            color = Color(0xFF2C3E50),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(selectedMembers) { member ->
                MemberAvatar(
                    member = member,
                    onRemove = { onRemoveMember(member) }
                )
            }

            item {
                // Add Member Button
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color(0xFFDDDDDD), CircleShape)
                        .background(Color.White)
                        .clickable { /* Handle add member */ },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Member",
                        tint = Color(0xFF666666),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MemberAvatar(
    member: userDTO,
    onRemove: () -> Unit
) {
    Box {
        // Column with Icon + Name, centered horizontally
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(64.dp) // Optional: to constrain width for long names
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C3E50)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = member.name,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = member.name,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }

        // Remove Button (X)
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(Color(0xFF999999))
                .align(Alignment.TopEnd)
                .clickable { onRemove() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Remove",
                tint = Color.White,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Composable
fun SearchContactsSection(
    searchQuery: String,
    contacts: List<userDTO>,
    onContactSelect: (userDTO) -> Unit,
    onSearchChange: (String) -> Unit
) {
    Column {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchChange,
            placeholder = {
                Text(
                    text = "Search contacts",
                    color = Color(0xFFCCCCCC)
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF999999)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2C3E50),
                unfocusedBorderColor = Color(0xFFDDDDDD),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            shape = RoundedCornerShape(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(modifier = Modifier.height(200.dp)) {
            val filteredContacts = contacts.filter {
                it.name.contains(searchQuery, ignoreCase = true)
            }

            if (filteredContacts.isEmpty()) {
                item {
                    Text(
                        text = "No contacts found",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray
                    )
                }
            } else {
                items(filteredContacts) { contact ->
                    ContactItem(contact = contact, onClick = onContactSelect)
                }
            }
        }
    }
}

@Composable
fun ContactItem(contact: userDTO, onClick: (userDTO) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(contact) }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF2C3E50)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, tint = Color.White)
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = contact.name, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun GroupCreatedDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Success!",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF2C3E50),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "The group was created successfully.",
                fontSize = 16.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2C3E50)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text(
                    text = "OK",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        modifier = Modifier
            .widthIn(max = 320.dp)
            .clip(RoundedCornerShape(12.dp)),
        containerColor = Color.White,
        tonalElevation = 8.dp
    )
}

@Composable
fun CreateGroupBottomNav(
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