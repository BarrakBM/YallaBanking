package com.joincoded.bankapi.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joincoded.bankapi.viewmodel.BankViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YallaBankingProfileScreen(viewModel: BankViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Profile",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF2C3E50),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
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
                .padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Profile Header Section
            item {
                ProfileHeaderSection()
//                ProfileHeaderSection(
//                    fullName = viewModel.fullName,
//                    username = viewModel.token?.username ?: "@username"
//                )
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Profile Information Section
            item {
                ProfileInformationSection()

            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Settings Options Section
            item {
                SettingsOptionsSection()
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
//fullName: String, username: String
fun ProfileHeaderSection() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0)),
            contentAlignment = Alignment.Center
        ) {
            // Simple avatar representation
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Avatar",
                modifier = Modifier.size(60.dp),
                tint = Color(0xFF666666)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = "Fatma Alnaseeb",
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C3E50)
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Username
        Text(
            text = "@falnaseeb",
            fontSize = 16.sp,
            color = Color(0xFF999999)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Edit Profile Button
        Button(
            onClick = { /* Handle edit profile */ },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2C3E50)
            ),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Text(
                text = "Edit Profile",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileInformationSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            ProfileInfoItem(
                icon = Icons.Outlined.Email,
                label = "Email",
                value = "fatma@gmail.com"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoItem(
                icon = Icons.Outlined.Phone,
                label = "Phone",
                value = "+965 22 111 6565"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileInfoItem(
                icon = Icons.Outlined.DateRange,
                label = "Member since",
                value = "Jan 14, 2025"
            )
        }
    }
}

@Composable
fun ProfileInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF666666),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                fontWeight = FontWeight.Normal
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Composable
fun SettingsOptionsSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            SettingsMenuItem(
                icon = Icons.Outlined.Lock,
                title = "Change Password",
                onClick = { /* Handle change password */ }
            )

//            SettingsMenuItem(
//                icon = Icons.Outlined.Call,
//                title = "Manage Cards",
//                onClick = { /* Handle manage cards */ }
//            )



            SettingsMenuItem(
                icon = Icons.Outlined.ExitToApp,
                title = "Log Out",
                onClick = { /* Handle log out */ },
                showDivider = false
            )
        }
    }
}

@Composable
fun SettingsMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    showDivider: Boolean = true
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF666666),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                fontSize = 16.sp,
                color = Color(0xFF2C3E50),
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = "Navigate",
                tint = Color(0xFF999999),
                modifier = Modifier.size(20.dp)
            )
        }

        if (showDivider) {
            Divider(
                color = Color(0xFFF0F0F0),
                thickness = 1.dp,
                modifier = Modifier.padding(start = 60.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YallaBankingProfileScreenPreview() {
    MaterialTheme {
        YallaBankingProfileScreen()
    }
}