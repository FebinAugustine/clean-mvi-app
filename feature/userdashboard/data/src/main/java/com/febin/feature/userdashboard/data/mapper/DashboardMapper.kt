package com.febin.feature.userdashboard.data.mapper

import com.febin.feature.userdashboard.data.dto.network.ActivityItemDto
import com.febin.feature.userdashboard.data.dto.network.MetricsDto
import com.febin.feature.userdashboard.data.dto.network.UserDashboardResponseDto
import com.febin.feature.userdashboard.data.dto.network.UserDto
import com.febin.feature.dashboard.domain.model.ActivityItem
import com.febin.feature.dashboard.domain.model.DashboardData
import com.febin.feature.dashboard.domain.model.DashboardError
import com.febin.feature.dashboard.domain.model.UserMetrics
import com.febin.shared_domain.model.Role
import com.febin.shared_domain.model.User

/**
 * Mappers for Dashboard DTOs ↔ Domain.
 */
object DashboardMapper {

    fun toDomain(dto: UserDashboardResponseDto): DashboardData = DashboardData(
        user = toDomain(dto.user),
        metrics = toDomain(dto.metrics),
        recentActivity = dto.recentActivity.map { toDomain(it) },
        notifications = dto.notifications
    )

    private fun toDomain(dto: UserDto): User = User(
        id = dto.id,
        email = dto.email,
        name = dto.name,
        role = Role.USER,  // Default for user dashboard
        isVerified = true  // Assume from auth
    )

    private fun toDomain(dto: MetricsDto): UserMetrics = UserMetrics(
        totalLogins = dto.totalLogins,
        lastLogin = dto.lastLogin,
        points = dto.points,
        level = dto.level
    )

    private fun toDomain(dto: ActivityItemDto): ActivityItem = ActivityItem(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        timestamp = dto.timestamp
    )

    // Error mapping
    fun mapToDashboardError(throwable: Throwable, httpCode: Int? = null): DashboardError = when {
        httpCode == 404 -> DashboardError.NoDataAvailable
        httpCode == 0 || throwable.message?.contains("network") == true -> DashboardError.OfflineMode
        else -> DashboardError.SyncFailed(throwable.message ?: "Dashboard sync failed")
    }
}