package com.febin.feature.admindashboard.data.repository

import com.febin.feature.admindashboard.data.mapper.AdminMapper
import com.febin.feature.admindashboard.data.remote.api.AdminDashboardApi
import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.feature.admindashboard.domain.model.ReportItem  // Added: Import for ReportItem fields
import com.febin.feature.admindashboard.domain.repository.AdminDashboardRepository
import com.febin.feature.admindashboard.data.local.dao.AdminDao
import com.febin.feature.admindashboard.data.local.entity.AdminMetricsEntity
import com.febin.feature.admindashboard.data.local.entity.ReportEntity
import com.febin.feature.admindashboard.domain.model.ReportStatus
import com.febin.shared_domain.model.Result
import kotlinx.coroutines.flow.first

/**
 * Impl of AdminDashboardRepository.
 * - Cache-first: Local query, fallback to remote + cache.
 */
class AdminDashboardRepositoryImpl(
    private val api: AdminDashboardApi,
    private val dao: AdminDao
) : AdminDashboardRepository {

    override suspend fun getAdminMetrics(): Result<AdminMetrics> {
        return try {
            // Try local first
            val localMetrics = dao.getAdminMetrics().first()
            if (localMetrics != null) {
                // Reconstruct (simplified)
                val reports = dao.getPendingReports().first().map { reportEntity ->
                    // Map ReportEntity to ReportItem (add to mapper if needed)
                    ReportItem(
                        id = reportEntity.id,
                        title = reportEntity.title,
                        description = reportEntity.description,
                        status = when (reportEntity.status) {
                            "RESOLVED" -> ReportStatus.RESOLVED
                            "REJECTED" -> ReportStatus.REJECTED
                            else -> ReportStatus.PENDING
                        },
                        timestamp = reportEntity.timestamp
                    )
                }
                val metrics = AdminMetrics(
                    totalUsers = localMetrics.totalUsers,
                    activeUsers = localMetrics.activeUsers,
                    pendingReports = reports,
                    recentAdmins = emptyList(),  // From separate query if needed
                    systemUptime = localMetrics.systemUptime
                )
                return Result.success(metrics)
            }

            // Fallback to remote
            val response = api.getAdminMetrics()
            val domainMetrics = AdminMapper.toDomain(response)

            // Cache
            cacheAdminMetrics(domainMetrics)

            Result.success(domainMetrics)
        } catch (e: Exception) {
            Result.failure(AdminMapper.mapToAdminDashboardError(e))
        }
    }

    override suspend fun refreshAdminMetrics(): Result<AdminMetrics> {
        return getAdminMetrics()  // Force refresh by clearing cache first in prod
    }

    override suspend fun resolveReport(reportId: String): Result<Unit> = try {
        // Update local/remote
        dao.getPendingReports().first().find { it.id == reportId }?.let { report ->
            dao.insertReports(listOf(report.copy(status = "RESOLVED")))
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun cacheAdminMetrics(metrics: AdminMetrics) {
        // Cache metrics
        dao.insertAdminMetrics(
            AdminMetricsEntity(
                totalUsers = metrics.totalUsers,
                activeUsers = metrics.activeUsers,
                systemUptime = metrics.systemUptime
            )
        )
        // Cache reports
        dao.insertReports(
            metrics.pendingReports.map { report ->  // report is ReportItem
                ReportEntity(
                    id = report.id,  // Now resolves with import
                    title = report.title,
                    description = report.description,
                    status = report.status.name,
                    timestamp = report.timestamp
                )
            }
        )
    }
}