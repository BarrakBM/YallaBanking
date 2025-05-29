package com.joincoded.bankapi.AppNavigator

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.joincoded.bankapi.composables.YallaBankingLoginScreen
import com.joincoded.bankapi.viewmodel.BankViewModel

object AppDestinations {
    const val lOGIN = "logIn"
    const val SIGNUP ="signUp"
    const val HOMEPAGE = "homePage"
    const val CARDS = "cards"
    const val CARDDETAILS ="cardDetails"
    const val TRANSFER ="tranfer"
    const val GROUPS ="groups"
    const val CREATEGROUP ="createGroup"
    const val GROUPDETAIL = "groupDetail/{id}"
    const val ADDMEMBER = "addMember"
    const val FUNDGROUP = "fundGroup"
    const val PROFILE = "profile"

    fun groupDetail(id: Int) = "group_detail/$id"
}

@Composable
fun BankNavHost(
    modifier: Modifier,
    navController:
    NavHostController = rememberNavController()){

    val bankViewModel: BankViewModel = viewModel()
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppDestinations.lOGIN
    ){
        composable(AppDestinations.lOGIN){
            YallaBankingLoginScreen(
                {
                    navController.navigate(AppDestinations.HOMEPAGE)
                },
                bankViewModel
            )
        }


    }
}