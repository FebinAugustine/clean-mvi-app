package com.febin.feature.admindashboard.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.febin.core.navigation.NavRoutes
import com.febin.feature.admindashboard.presentation.screen.AdminDashboardScreen

fun NavGraphBuilder.adminDashboardNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "dashboard",
        route = NavRoutes.AdminDashboard.route
    ) {
        composable("dashboard") {
            AdminDashboardScreen(navController = navController)
        }
    }
}