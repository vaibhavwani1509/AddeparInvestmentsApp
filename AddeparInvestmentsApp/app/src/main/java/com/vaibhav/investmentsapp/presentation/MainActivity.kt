package com.vaibhav.investmentsapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.vaibhav.investmentsapp.presentation.screens.details.InvestmentDetailsScreen
import com.vaibhav.investmentsapp.presentation.screens.home.InvestmentsHomeScreen
import com.vaibhav.investmentsapp.presentation.theme.InvestmentsAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InvestmentsAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()
                    NavHost(
                        modifier = Modifier.padding(innerPadding),
                        navController = navController,
                        startDestination = AppScreens.Home.route
                    ) {
                        composable(
                            route = AppScreens.Home.route
                        ) {
                            InvestmentsHomeScreen(
                                navController = navController
                            )
                        }

                        composable(
                            route = AppScreens.Details.route,
                            arguments = listOf(
                                navArgument(
                                    name = "investment"
                                ) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val investment = backStackEntry.arguments?.getString("investment")
                            if (investment != null) {
                                InvestmentDetailsScreen(
                                    navController = navController,
                                    investmentName = investment,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}