package com.febin.feature.userdashboard.data.mapper

import com.febin.core.data.local.entity.ActivityEntity
import com.febin.core.data.local.entity.DashboardEntity
import com.febin.feature.dashboard.domain.model.ActivityItem
import com.febin.feature.dashboard.domain.model.DashboardData
import com.febin.feature.dashboard.domain.model.DashboardError
import com.febin.feature.dashboard.domain.model.UserMetrics
import com.febin.feature.userdashboard.data.dto.network.ActivityItemDto
import com.febin.feature.userdashboard.data.dto.network.MetricsDto
import com.febin.feature.userdashboard.data.dto.network.UserDashboardResponseDto
import com.febin.feature.userdashboard.data.dto.network.UserDto
import com.febin.shared_domain.model.Role
import com.febin.shared_domain.model.User

object DashboardMapper {

    // DTO to Domain
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
        role = Role.USER,
        isVerified = true
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

    fun toDomain(entity: ActivityEntity): ActivityItem = ActivityItem(
        id = entity.id,
        title = entity.title,
        description = entity.description,
        timestamp = entity.timestamp
    )

    // Domain to Entity
    fun fromDomain(domain: DashboardData, userId: String): DashboardEntity = DashboardEntity(
        userId = userId,
        totalLogins = domain.metrics.totalLogins,
        lastLogin = domain.metrics.lastLogin,
        points = domain.metrics.points,
        level = domain.metrics.level,
        notifications = domain.notifications
    )

    fun fromDomain(domain: ActivityItem, userId: String): ActivityEntity = ActivityEntity(
        id = domain.id,
        userId = userId,
        title = domain.title,
        description = domain.description,
        timestamp = domain.timestamp
    )

    // Error mapping
    fun mapToDashboardError(throwable: Throwable, httpCode: Int? = null): DashboardError = when {
        httpCode == 404 -> DashboardError.NoDataAvailable
        httpCode == 0 || throwable.message?.contains("network") == true -> DashboardError.OfflineMode
        else -> DashboardError.SyncFailed(throwable.message ?: "Dashboard sync failed")
    }
}