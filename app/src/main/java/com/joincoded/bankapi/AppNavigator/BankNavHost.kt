package com.joincoded.bankapi.AppNavigator

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joincoded.bankapi.composables.*
import com.joincoded.bankapi.viewmodel.BankViewModel

object AppDestinations {
    const val lOGIN = "logIn"
    const val CREATEPROFILE = "createProfile"
    const val SIGNUP ="signUp"
    const val HOMEPAGE = "homePage"
    const val CARDS = "cards"
    const val CARDDETAILS ="cardDetails"
    const val TRANSFER ="transfer"
    const val GROUPS ="groups"
    const val CREATEGROUP ="createGroup"
    const val GROUPDETAIL = "groupDetail/{id}"
    const val ADDMEMBER = "addMember"
    const val FUNDGROUP = "fundGroup"
    const val PROFILE = "profile"

    fun groupDetail(id: Long) = "groupDetail/$id"
}

@Composable
fun BankNavHost(
    modifier: Modifier,
    navController: NavHostController = rememberNavController()
) {
    val bankViewModel: BankViewModel = viewModel()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppDestinations.lOGIN
    ) {
        composable(AppDestinations.lOGIN) {
            YallaBankingLoginScreen(
                bankViewModel = bankViewModel,
                navController = navController
            )
        }

        composable(AppDestinations.SIGNUP) {
            SignUpScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }

        composable(AppDestinations.CREATEPROFILE) {
            CreateProfileScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }

        composable(AppDestinations.HOMEPAGE) {
            HomeScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }

        composable(AppDestinations.CARDS) {
            CardsScreen(
                viewModel = bankViewModel,
                navController = navController,
                onNavigateToCardDetails = {
                    navController.navigate(AppDestinations.CARDDETAILS)
                }
            )
        }

        composable(AppDestinations.CARDDETAILS) {
            CardDetailsScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }

        composable(AppDestinations.TRANSFER) {
            TransferScreen(
                viewModel = bankViewModel,
                navController = navController,
            )
        }

        composable(AppDestinations.GROUPS) {
            GroupsScreen(
                viewModel = bankViewModel,
                navController = navController,
            )
        }

        composable(AppDestinations.CREATEGROUP) {
            CreateGroupScreen(
                bankViewModel = bankViewModel,
                navController= navController
            )
        }

        composable(AppDestinations.GROUPDETAIL) {
            val groupId = it.arguments?.getString("id")?.toLongOrNull() ?: 0L
            GroupDetailsScreen(
                groupId = groupId,
                viewModel = bankViewModel,
                navController = navController
            )
        }

        composable(AppDestinations.ADDMEMBER) {
            Text(text = "Add Member Screen - To be implemented")
        }

        composable(AppDestinations.FUNDGROUP) {
            val group = bankViewModel.selectedGroup
            if (group != null) {
                FundGroupScreen(
                    viewModel = bankViewModel,
                    navController = navController,
                    group = group
                )
            } else {
                LaunchedEffect(Unit) {
                    navController.navigate(AppDestinations.GROUPS) {
                        popUpTo(AppDestinations.FUNDGROUP) { inclusive = true }
                    }
                }
            }
        }

        composable(AppDestinations.PROFILE) {
            ProfileScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }
    }
}