package com.febin.core.navigation

sealed class NavRoutes(val route: String) {
    // Core routes
    object Splash : NavRoutes("splash")
    object Onboarding : NavRoutes("onboarding")

    // Feature routes
    object Auth : NavRoutes("auth")
    object UserDashboard : NavRoutes("user_dashboard")
    object AdminDashboard : NavRoutes("admin_dashboard")
    object Profile : NavRoutes("profile")
}