package com.febin.feature.userdashboard.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.febin.core.navigation.NavRoutes
import com.febin.feature.userdashboard.presentation.screen.UserDashboardScreen
import com.febin.feature.userdashboard.presentation.screen.UserProfileScreen

/**
 * Feature-specific NavGraph for User Dashboard.
 * - Routes: dashboard, profile.
 */
@Composable
fun UserDashboardNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = "dashboard",
        route = NavRoutes.UserDashboard.route
    ) {
        composable("dashboard") {
            UserDashboardScreen(navController = navController)
        }
        composable("profile") {
            UserProfileScreen(navController = navController)
        }
    }
}

fun NavGraphBuilder.userDashboardNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "dashboard",
        route = NavRoutes.UserDashboard.route
    ) {
        composable("dashboard") {
            UserDashboardScreen(navController = navController)
        }
        composable("profile") {
            UserProfileScreen(navController = navController)
        }
    }
}