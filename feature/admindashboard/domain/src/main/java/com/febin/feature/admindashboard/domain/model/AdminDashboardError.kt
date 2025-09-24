package com.febin.feature.admindashboard.domain.model

/**
 * Sealed class for admin dashboard-specific errors.
 */
sealed class AdminDashboardError : Exception() {
    data object NoAdminAccess : AdminDashboardError() {
        private fun readResolve(): Any = NoAdminAccess
    }

    data class ReportFetchFailed(override val message: String) : AdminDashboardError()
    data object OfflineMode : AdminDashboardError() {
        private fun readResolve(): Any = OfflineMode
    }
}