package com.joincoded.bankapi.AppNavigator


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

    fun groupDetail(id: Int) = "groupDetail/$id"
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
        // Login Screen
        composable(AppDestinations.lOGIN) {
            YallaBankingLoginScreen(
                bankViewModel = bankViewModel,
                navController = navController
            )
        }

        // Sign Up Screen
        composable(AppDestinations.SIGNUP) {
            SignUpScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }

        // Create Profile Screen (for new users after login)
        composable(AppDestinations.CREATEPROFILE) {
            Text(text = "change me")
        }


        // Fund Group Screen

        // Home Screen
        composable(AppDestinations.HOMEPAGE) {
            HomeScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }

        // Cards Screen
        composable(AppDestinations.CARDS) {
            CardsScreen(
                viewModel = bankViewModel,
                navController = navController,
                onNavigateToCardDetails = {
                    navController.navigate(AppDestinations.CARDDETAILS)
                }
            )
        }

        // Card Details Screen
        composable(AppDestinations.CARDDETAILS) {
            Text(text = "change me")
        }

        // Transfer Screen
        composable(AppDestinations.TRANSFER) {
            TransferScreen(
                viewModel = bankViewModel,
                navController = navController,
            )
        }

        // Groups Screen
        composable(AppDestinations.GROUPS) {
            GroupsScreen(
                viewModel = bankViewModel,
                navController = navController,
            )
        }

        // Create Group Screen
        composable(AppDestinations.CREATEGROUP) {
            Text(text = "change me")
        }

        // Group Details Screen
        composable(AppDestinations.GROUPDETAIL) {
            // Extract group ID from navigation arguments
            val groupId = it.arguments?.getString("id")?.toIntOrNull() ?: 0
            Text(text = "change me")
        }

        // Add Member Screen
        composable(AppDestinations.ADDMEMBER) {
            Text(text = "change me")
        }

        // Fund Group Screen
        composable(AppDestinations.FUNDGROUP) {
            val group = bankViewModel.selectedGroup
            FundGroupScreen(
                viewModel = bankViewModel,
                navController = navController,
                group = group!!
            )
        }

        // Profile Screen
        composable(AppDestinations.PROFILE) {
            ProfileScreen(
                viewModel = bankViewModel,
                navController = navController
            )
        }
    }
}


//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.joincoded.bankapi.composables.CardsScreen
//import com.joincoded.bankapi.composables.CompleteProfileScreen
//import com.joincoded.bankapi.composables.YallaBankingLoginScreen
//import com.joincoded.bankapi.composables.YallaBankingProfileScreen
//import com.joincoded.bankapi.composables.YallaBankingSignUpScreen
//import com.joincoded.bankapi.viewmodel.BankViewModel
//
//
//object AppDestinations {
//    const val lOGIN = "logIn"
//    const val CREATEPROFILE = "createProfile"
//    const val SIGNUP ="signUp"
//    const val HOMEPAGE = "homePage"
//    const val CARDS = "composable/cards"
//    const val CARDDETAILS ="cardDetails"
//    const val TRANSFER ="tranfer"
//    const val GROUPS ="groups"
//    const val CREATEGROUP ="createGroup"
//    const val GROUPDETAIL = "groupDetail/{id}"
//    const val ADDMEMBER = "addMember"
//    const val FUNDGROUP = "fundGroup"
//    const val PROFILE = "profile"
//
//    fun groupDetail(id: Int) = "group_detail/$id"
//}
//
//@Composable
//fun BankNavHost(
//    modifier: Modifier,
//    navController:
//    NavHostController = rememberNavController()){
//
//    val bankViewModel: BankViewModel = viewModel()
//    NavHost(
//        modifier = modifier,
//        navController = navController,
//        startDestination = AppDestinations.lOGIN
//    ){
//        composable(AppDestinations.lOGIN){
//            YallaBankingLoginScreen(
//
//                bankViewModel,
//                navController
//            )
//
//
//        }
////        composable(AppDestinations.CREATEPROFILE) {
////            CompleteProfileScreen(
////                bankViewModel,
////                {
////                    navController.navigate(AppDestinations.SIGNUP)
////                }
////                )
////        }
//        composable(AppDestinations.CREATEPROFILE) {
//            CompleteProfileScreen(
//                bankViewModel,
//                onNavigateToSignup = {
//                    navController.navigate(AppDestinations.SIGNUP)
//                },
//                onNavigateToHome = {
//                    navController.navigate(AppDestinations.CARDS)  // or wherever you want to navigate after creating profile
//                }
//            )
//        }
//
//
//        composable (AppDestinations.SIGNUP){
//            YallaBankingSignUpScreen(
//            bankViewModel,
//            modifier,
//            {
//                navController.navigate(AppDestinations.CREATEPROFILE)
//            }
//            )
//        }
//
//
//        composable (AppDestinations.HOMEPAGE){
//
//            CardsScreen(bankViewModel,{
//                navController.navigate(AppDestinations.CARDDETAILS)
//            })
//        }
//        composable (AppDestinations.CARDS){
//            Text(text = "Card Detail")
//        }
//
//        composable (AppDestinations.CARDDETAILS){
//
//
//        }
//
//        composable (AppDestinations.TRANSFER){
//
//
//        }
//        composable (AppDestinations.GROUPS){
//
//        }
//        composable (AppDestinations.CREATEGROUP){
//
//        }
//        composable (AppDestinations.GROUPDETAIL){
//
//        }
//        composable (AppDestinations.ADDMEMBER){
//
//        }
//        composable (AppDestinations.FUNDGROUP){
//
//        }
//        composable (AppDestinations.PROFILE){
//
//        }
//
//
//    }
//}
//
//
//
////import androidx.compose.runtime.Composable
////import androidx.compose.ui.Modifier
////import androidx.lifecycle.viewmodel.compose.viewModel
////import androidx.navigation.NavHostController
////import androidx.navigation.compose.NavHost
////import androidx.navigation.compose.composable
////import androidx.navigation.compose.rememberNavController
////import com.joincoded.bankapi.viewmodel.BankViewModel
////import com.joincoded.bankapi.composables.CompleteProfileScreen
////object AppDestinations {
////    const val lOGIN = "logIn"
////    const val SIGNUP ="signUp"
////    const val HOMEPAGE = "homePage"
////    const val CARDS = "cards"
////    const val CARDDETAILS ="cardDetails"
////    const val TRANSFER ="tranfer"
////    const val GROUPS ="groups"
////    const val CREATEGROUP ="createGroup"
////    const val GROUPDETAIL = "groupDetail/{id}"
////    const val ADDMEMBER = "addMember"
////    const val FUNDGROUP = "fundGroup"
////    const val PROFILE = "profile"
////    const val COMPLETEPROFILE = "complete profile"
////
////    fun groupDetail(id: Int) = "group_detail/$id"
////}
////
////@Composable
////fun BankNavHost(modifier: Modifier,
////                navController:NavHostController = rememberNavController()) {
////    val viewModel: BankViewModel = viewModel()
////    NavHost(
////        modifier = modifier,
////        navController = navController,
////        startDestination = AppDestinations.COMPLETEPROFILE
////    ) {
//////        composable(AppDestinations.SIGNUP) {
//////            YallaBankingSignUpScreen()
//////        }
////        composable(AppDestinations.COMPLETEPROFILE) {
////            CompleteProfileScreen(
////                viewModel = viewModel,
////                onNavigateToSignup = {
////                    navController.navigate(AppDestinations.SIGNUP)
////                }
////            )
////        }
////    }
//////    composable(AppDestinations.SIGNUP) {
//////        YallaBankingSignUpScreen(
//////            viewModel,
//////            modifier,
//////            {
//////                navController.navigate(AppDestinations.COMPLETEPROFILE)
//////            }
//////        )
//////
//////    }
////}
////
////
////
////
////
////
////
//////        composable(AppDestinations.SIGNUP) {
//////            YallaBankingSignUpScreen(navController.navigate(AppDestinations.lOGIN))
//////        }
////