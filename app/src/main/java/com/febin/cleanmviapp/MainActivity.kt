package com.febin.cleanmviapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.febin.core.navigation.NavRoutes
import com.febin.core.ui.screens.OnboardingScreen
import com.febin.core.ui.screens.SplashScreen
import com.febin.core.ui.theme.AppTheme
import com.febin.di.SharedPreferencesManager
import com.febin.feature.admindashboard.presentation.navigation.adminDashboardNavGraph
import com.febin.feature.authentication.presentation.navigation.authNavGraph
import com.febin.feature.userdashboard.presentation.navigation.userDashboardNavGraph
import com.febin.shared_domain.model.Role
import kotlinx.coroutines.delay
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AppTheme {
                val navController = rememberNavController()
                val prefsManager = koinInject<SharedPreferencesManager>()
                var currentUserRole by remember { mutableStateOf<Role?>(null) }  // Demo: From auth repo in prod

                // Simulate role fetch (in prod: from AuthRepository)
                LaunchedEffect(Unit) {
                    delay(1000)  // Mock delay
                    currentUserRole = Role.USER  // Or ADMIN based on token
                }

                NavHost(
                    navController = navController,
                    startDestination = NavRoutes.Splash.route
                ) {
                    composable(NavRoutes.Splash.route) {
                        SplashScreen {
                            // Check onboarding status
                            val isOnboarded = prefsManager.isOnboardingCompleted()
                            if (isOnboarded) {
                                if (currentUserRole != null) {
                                    when (currentUserRole) {
                                        Role.ADMIN -> navController.navigate(NavRoutes.AdminDashboard.route) {
                                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                                        }
                                        else -> navController.navigate(NavRoutes.UserDashboard.route) {
                                            popUpTo(NavRoutes.Splash.route) { inclusive = true }
                                        }
                                    }
                                } else {
                                    navController.navigate(NavRoutes.Auth.route) {
                                        popUpTo(NavRoutes.Splash.route) { inclusive = true }
                                    }
                                }
                            } else {
                                navController.navigate(NavRoutes.Onboarding.route) {
                                    popUpTo(NavRoutes.Splash.route) { inclusive = true }
                                }
                            }
                        }
                    }

                    composable(NavRoutes.Onboarding.route) {
                        OnboardingScreen {
                            // Mark as onboarded and navigate
                            prefsManager.setOnboardingCompleted(true)
                            navController.navigate(NavRoutes.Auth.route) {
                                popUpTo(NavRoutes.Onboarding.route) { inclusive = true }
                            }
                        }
                    }

                    authNavGraph(navController)

                    userDashboardNavGraph(navController)

                    adminDashboardNavGraph(navController)
                }
            }
        }
    }
}