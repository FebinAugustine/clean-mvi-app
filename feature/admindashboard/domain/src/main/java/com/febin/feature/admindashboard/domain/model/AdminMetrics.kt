package com.febin.feature.admindashboard.domain.model


import com.febin.shared_domain.model.User

/**
 * Domain model for Admin Dashboard metrics.
 * - Aggregates admin stats like user counts, reports; immutable.
 */
data class AdminMetrics(
    val totalUsers: Int,
    val activeUsers: Int,
    val pendingReports: List<ReportItem>,
    val recentAdmins: List<User>,  // Shared User for admin users
    val systemUptime: String?  // e.g., "99.9%"
)

/**
 * Nested model for pending reports.
 */
data class ReportItem(
    val id: String,
    val title: String,
    val description: String,
    val status: ReportStatus,
    val timestamp: String  // ISO date
)

/**
 * Enum for report status.
 */
enum class ReportStatus {
    PENDING,
    RESOLVED,
    REJECTED
}