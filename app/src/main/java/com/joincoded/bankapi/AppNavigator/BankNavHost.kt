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
import com.joincoded.bankapi.composables.CreateGroupScreen
import com.joincoded.bankapi.composables.YallaBankingLoginScreen
import com.joincoded.bankapi.viewmodel.BankViewModel

object AppDestinations {
    const val lOGIN = "logIn"
    const val CREATEPROFILE = "createProfile"
    const val SIGNUP ="signUp"
    const val HOMEPAGE = "homePage"
    const val CARDS = "composable/cards"
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

                bankViewModel,
                navController
            )


        }

        composable (AppDestinations.SIGNUP){


            Text(text = "Sign Up")
        }


//        composable (AppDestinations.HOMEPAGE){
//
//            CardsScreen(bankViewModel,{
//                navController.navigate(AppDestinations.CARDDETAILS)
//            })
//        }
        composable (AppDestinations.CARDS){
            Text(text = "Card Detail")
        }

        composable (AppDestinations.CARDDETAILS){


        }

        composable (AppDestinations.TRANSFER){


        }
        composable (AppDestinations.GROUPS){

        }
        composable (AppDestinations.HOMEPAGE){   //////////////////////////////////////////////////////

            CreateGroupScreen(bankViewModel)

        }
        composable (AppDestinations.GROUPDETAIL){

        }
        composable (AppDestinations.ADDMEMBER){

        }
        composable (AppDestinations.FUNDGROUP){

        }
        composable (AppDestinations.PROFILE){

        }


    }
}