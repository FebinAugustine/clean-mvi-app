package com.febin.feature.admindashboard.data.repository

import com.febin.core.data.local.dao.AdminDao
import com.febin.feature.admindashboard.data.mapper.AdminMapper
import com.febin.feature.admindashboard.data.remote.api.AdminDashboardApi
import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.feature.admindashboard.domain.model.ReportItem
import com.febin.feature.admindashboard.domain.model.ReportStatus
import com.febin.feature.admindashboard.domain.repository.AdminDashboardRepository
import com.febin.shared_domain.model.Result
import kotlinx.coroutines.flow.first

class AdminDashboardRepositoryImpl(
    private val api: AdminDashboardApi,
    private val dao: AdminDao
) : AdminDashboardRepository {

    override suspend fun getAdminMetrics(): Result<AdminMetrics> {
        return try {
            val localMetrics = dao.getAdminMetrics().first()
            if (localMetrics != null) {
                val reports = dao.getPendingReports().first().map { reportEntity ->
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
                    recentAdmins = emptyList(),
                    systemUptime = localMetrics.systemUptime
                )
                return Result.success(metrics)
            }

            val response = api.getAdminMetrics()
            val domainMetrics = AdminMapper.toDomain(response)

            cacheAdminMetrics(domainMetrics)

            Result.success(domainMetrics)
        } catch (e: Exception) {
            Result.failure(AdminMapper.mapToAdminDashboardError(e))
        }
    }

    override suspend fun refreshAdminMetrics(): Result<AdminMetrics> {
        return getAdminMetrics()
    }

    override suspend fun resolveReport(reportId: String): Result<Unit> = try {
        dao.getPendingReports().first().find { it.id == reportId }?.let { report ->
            dao.insertReports(listOf(report.copy(status = "RESOLVED")))
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun cacheAdminMetrics(metrics: AdminMetrics) {
        dao.insertAdminMetrics(AdminMapper.fromDomain(metrics))
        dao.insertReports(metrics.pendingReports.map { AdminMapper.fromDomain(it) })
    }
}