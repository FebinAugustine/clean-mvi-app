package com.febin.feature.admindashboard.presentation.state

import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.feature.admindashboard.domain.model.AdminDashboardError

/**
 * Data class for AdminDashboard MVI state.
 * - Includes metrics and loading/error.
 */
data class AdminDashboardState(
    val metrics: AdminMetrics? = null,
    val isLoading: Boolean = false,
    val error: AdminDashboardError? = null
) {
    val hasData: Boolean = metrics != null
    val pendingReportsCount: Int = metrics?.pendingReports?.size ?: 0
}