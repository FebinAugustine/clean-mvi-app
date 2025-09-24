package com.febin.feature.dashboard.domain.model


import com.febin.shared_domain.model.User

/**
 * Domain model for User Dashboard data.
 * - Aggregates user metrics, recent activity; reusable in UI.
 * - Immutable; fetched via use case.
 */
data class DashboardData(
    val user: User,  // Shared from shared-domain
    val metrics: UserMetrics,
    val recentActivity: List<ActivityItem>,
    val notifications: Int = 0
)

/**
 * Nested model for user metrics (e.g., stats).
 */
data class UserMetrics(
    val totalLogins: Int,
    val lastLogin: String?,  // ISO date string
    val points: Int,
    val level: Int
)

/**
 * Nested model for recent activity items.
 */
data class ActivityItem(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String  // ISO date
)

/**
 * Sealed class for dashboard-specific errors.
 */
sealed class DashboardError : Exception() {
    data object NoDataAvailable : DashboardError() {
        private fun readResolve(): Any = NoDataAvailable
    }

    data class SyncFailed(override val message: String) : DashboardError()
    data object OfflineMode : DashboardError() {
        private fun readResolve(): Any = OfflineMode
    }
}