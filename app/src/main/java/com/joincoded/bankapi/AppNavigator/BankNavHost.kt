package com.joincoded.bankapi.AppNavigator

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joincoded.bankapi.composables.CardsScreen
import com.joincoded.bankapi.composables.YallaBankingLoginScreen
import com.joincoded.bankapi.composables.CardDetailsScreen
import com.joincoded.bankapi.viewmodel.BankViewModel

object AppDestinations {
    const val lOGIN = "logIn"
    const val CREATEPROFILE = "createProfile"
    const val SIGNUP = "signUp"
    const val HOMEPAGE = "homePage"
    const val CARDS = "composable/cards"
    const val CARDDETAILS = "cardDetails"
    const val TRANSFER = "transfer"
    const val GROUPS = "groups"
    const val CREATEGROUP = "createGroup"
    const val GROUPDETAIL = "groupDetail/{id}"
    const val ADDMEMBER = "addMember"
    const val FUNDGROUP = "fundGroup"
    const val PROFILE = "profile"

    fun groupDetail(id: Int) = "groupDetail/$id"
}

@Composable
fun BankNavHost(
    navController: NavHostController = rememberNavController()
) {
    val bankViewModel: BankViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = AppDestinations.lOGIN
    ) {
        composable(AppDestinations.lOGIN) {
            YallaBankingLoginScreen(
                bankViewModel,
                navController
            )
        }

        composable(AppDestinations.SIGNUP) {
            Text(text = "Sign Up")
        }

        composable(AppDestinations.HOMEPAGE) {
            CardsScreen(
                viewModel = bankViewModel,
                onNavigateToCardDetails = {
                    navController.navigate(AppDestinations.CARDDETAILS)
                }
            )
        }

        composable(AppDestinations.CARDS) {
            CardsScreen(
                viewModel = bankViewModel,
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
            Text(text = "Transfer Screen - To be implemented")
        }

        composable(AppDestinations.GROUPS) {
            Text(text = "Groups Screen - To be implemented")
        }

        composable(AppDestinations.CREATEGROUP) {
            Text(text = "Create Group Screen - To be implemented")
        }

        composable(AppDestinations.GROUPDETAIL) {
            Text(text = "Group Detail Screen - To be implemented")
        }

        composable(AppDestinations.ADDMEMBER) {
            Text(text = "Add Member Screen - To be implemented")
        }

        composable(AppDestinations.FUNDGROUP) {
            Text(text = "Fund Group Screen - To be implemented")
        }

        composable(AppDestinations.PROFILE) {
            Text(text = "Profile Screen - To be implemented")
        }
    }
}