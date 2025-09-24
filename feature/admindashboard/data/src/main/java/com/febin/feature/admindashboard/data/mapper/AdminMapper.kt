package com.febin.feature.admindashboard.data.mapper

import com.febin.feature.admindashboard.data.dto.network.AdminResponseDto
import com.febin.feature.admindashboard.data.dto.network.AdminUserDto
import com.febin.feature.admindashboard.data.dto.network.ReportItemDto
import com.febin.feature.admindashboard.domain.model.AdminDashboardError
import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.feature.admindashboard.domain.model.ReportItem
import com.febin.feature.admindashboard.domain.model.ReportStatus
import com.febin.shared_domain.model.Role
import com.febin.shared_domain.model.User

/**
 * Mappers for Admin DTOs ↔ Domain.
 */
object AdminMapper {

    fun toDomain(dto: AdminResponseDto): AdminMetrics = AdminMetrics(
        totalUsers = dto.totalUsers,
        activeUsers = dto.activeUsers,
        pendingReports = dto.pendingReports.map { toDomain(it) },
        recentAdmins = dto.recentAdmins.map { toDomain(it) },
        systemUptime = dto.systemUptime
    )

    private fun toDomain(dto: ReportItemDto): ReportItem = ReportItem(
        id = dto.id,
        title = dto.title,
        description = dto.description,
        status = when (dto.status.uppercase()) {
            "RESOLVED" -> ReportStatus.RESOLVED
            "REJECTED" -> ReportStatus.REJECTED
            else -> ReportStatus.PENDING
        },
        timestamp = dto.timestamp
    )

    private fun toDomain(dto: AdminUserDto): User = User(
        id = dto.id,
        email = dto.email,
        name = dto.name,
        role = Role.ADMIN
    )

    // Error mapping
    fun mapToAdminDashboardError(throwable: Throwable, httpCode: Int? = null): AdminDashboardError = when {
        httpCode == 403 -> AdminDashboardError.NoAdminAccess
        httpCode == 404 -> AdminDashboardError.ReportFetchFailed("No data")
        httpCode == 0 || throwable.message?.contains("network") == true -> AdminDashboardError.OfflineMode
        else -> AdminDashboardError.ReportFetchFailed(throwable.message ?: "Admin fetch failed")
    }
}