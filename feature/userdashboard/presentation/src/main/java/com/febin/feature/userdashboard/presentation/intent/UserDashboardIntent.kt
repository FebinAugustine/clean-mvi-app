package com.febin.feature.userdashboard.presentation.intent

/**
 * Sealed class for UserDashboard MVI intents.
 * - Covers dashboard load/refresh and profile actions.
 */
sealed class UserDashboardIntent {
    object LoadDashboard : UserDashboardIntent()
    object RefreshDashboard : UserDashboardIntent()
    object ViewProfile : UserDashboardIntent()
    data class UpdateProfile(val name: String, val phone: String) : UserDashboardIntent()
    object ClearError : UserDashboardIntent()
}