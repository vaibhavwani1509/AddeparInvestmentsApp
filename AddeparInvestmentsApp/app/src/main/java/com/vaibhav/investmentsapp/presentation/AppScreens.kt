package com.vaibhav.investmentsapp.presentation

/**
 * Class to hold the navigation screens for the app.
 */
sealed class AppScreens(val route: String) {

    data object Home: AppScreens("home")
    data object Details: AppScreens("details/{investment}") {
        fun createRoute(investment: String) = "details/$investment"
    }
}