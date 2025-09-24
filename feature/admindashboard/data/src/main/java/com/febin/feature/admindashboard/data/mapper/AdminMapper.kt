package com.febin.feature.admindashboard.data.mapper

import com.febin.core.data.local.entity.AdminMetricsEntity
import com.febin.core.data.local.entity.ReportEntity
import com.febin.feature.admindashboard.data.dto.network.AdminResponseDto
import com.febin.feature.admindashboard.data.dto.network.ReportDto
import com.febin.feature.admindashboard.domain.model.AdminDashboardError
import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.feature.admindashboard.domain.model.ReportItem
import com.febin.feature.admindashboard.domain.model.ReportStatus
import com.febin.shared_domain.model.User

// Rewriting this file from scratch to ensure correctness and break any cache issues.
object AdminMapper {

    fun toDomain(dto: AdminResponseDto): AdminMetrics = AdminMetrics(
        totalUsers = dto.metrics.totalUsers,
        activeUsers = dto.metrics.activeUsers,
        pendingReports = dto.reports.map { toDomain(it) },
        recentAdmins = dto.admins.map { User(id = it.id, email = it.email, name = it.name) },
        systemUptime = dto.metrics.systemUptime
    )

    private fun toDomain(dto: ReportDto): ReportItem = ReportItem(
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

    fun fromDomain(domain: AdminMetrics): AdminMetricsEntity = AdminMetricsEntity(
        totalUsers = domain.totalUsers,
        activeUsers = domain.activeUsers,
        systemUptime = domain.systemUptime
    )

    fun fromDomain(domain: ReportItem): ReportEntity = ReportEntity(
        id = domain.id,
        title = domain.title,
        description = domain.description,
        status = domain.status.name,
        timestamp = domain.timestamp
    )

    fun mapToAdminDashboardError(throwable: Throwable, httpCode: Int? = null): AdminDashboardError = when {
        httpCode == 403 -> AdminDashboardError.NoAdminAccess
        throwable.message?.contains("network") == true -> AdminDashboardError.OfflineMode
        else -> AdminDashboardError.ReportFetchFailed(throwable.message ?: "An unknown error occurred")
    }
}