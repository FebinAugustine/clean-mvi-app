package com.febin.feature.userdashboard.presentation.state

import com.febin.feature.dashboard.domain.model.DashboardData
import com.febin.feature.dashboard.domain.model.DashboardError

/**
 * Data class for UserDashboard MVI state.
 * - Includes dashboard data and profile editing fields.
 */
data class UserDashboardState(
    val dashboardData: DashboardData? = null,
    val isLoading: Boolean = false,
    val error: DashboardError? = null,
    val isProfileEditing: Boolean = false,
    val profileName: String = "",
    val profilePhone: String = ""
) {
    val hasData: Boolean = dashboardData != null
    val notificationsCount: Int = dashboardData?.notifications ?: 0
}