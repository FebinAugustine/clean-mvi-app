package com.febin.feature.admindashboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.febin.core.navigation.NavRoutes
import com.febin.feature.admindashboard.presentation.screen.AdminDashboardScreen

/**
 * Feature-specific NavGraph for Admin Dashboard.
 * - Single screen for now.
 */
@Composable
fun AdminDashboardNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        route = NavRoutes.AdminDashboard.route
    ) {
        composable("dashboard") {
            AdminDashboardScreen(navController = navController)
        }
    }
}

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