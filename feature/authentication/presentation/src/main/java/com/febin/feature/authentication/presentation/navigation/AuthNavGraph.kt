package com.febin.feature.authentication.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.febin.core.navigation.NavRoutes
import com.febin.feature.authentication.presentation.screen.SigninScreen
import com.febin.feature.authentication.presentation.screen.SignupScreen

/**
 * Feature-specific NavGraph for Authentication.
 * - Composable; called from root NavHost.
 * - Routes: signin, signup.
 */
@Composable
fun AuthNavGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Auth.route,  // Or "signin"
        route = NavRoutes.Auth.route
    ) {
        composable("signin") {
            SigninScreen(navController = navController)
        }
        composable("signup") {
            SignupScreen(navController = navController)
        }
    }
}

/**
 * Extension for builder (if using navigation {} in root).
 */
fun NavGraphBuilder.authNavGraph(navController: NavHostController) {
    navigation(
        startDestination = "signin",
        route = NavRoutes.Auth.route
    ) {
        composable("signin") {
            SigninScreen(navController = navController)
        }
        composable("signup") {
            SignupScreen(navController = navController)
        }
    }
}