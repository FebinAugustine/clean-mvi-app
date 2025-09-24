package com.febin.feature.admindashboard.presentation.intent


/**
 * Sealed class for AdminDashboard MVI intents.
 * - Covers load/refresh and report actions.
 */
sealed class AdminDashboardIntent {
    object LoadMetrics : AdminDashboardIntent()
    object RefreshMetrics : AdminDashboardIntent()
    data class ResolveReport(val reportId: String) : AdminDashboardIntent()
    object ClearError : AdminDashboardIntent()
}