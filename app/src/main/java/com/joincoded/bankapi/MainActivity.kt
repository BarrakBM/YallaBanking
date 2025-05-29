package com.joincoded.bankapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.joincoded.bankapi.AppNavigator.BankNavHost
import com.joincoded.bankapi.composable.CompleteProfileScreen
import com.joincoded.bankapi.composable.YallaBankingProfileScreen
import com.joincoded.bankapi.composable.YallaBankingSignUpScreen
import com.joincoded.bankapi.ui.theme.BankAPITheme
import com.joincoded.bankapi.viewmodel.BankViewModel
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BankAPITheme {
                val navController = rememberNavController()
                val viewModel: BankViewModel = viewModel()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

//                    YallaBankingSignUpScreen(
//
//                        viewModel = viewModel,
//                        modifier = Modifier.padding(innerPadding)
//                    )
                    BankNavHost(
                        modifier = Modifier.padding(innerPadding),
                    )

//                    CompleteProfileScreen(
//                        viewModel = viewModel
//                    )
                    //YallaBankingProfileScreen( viewModel = viewModel)


                }
            }
        }
    }
}



