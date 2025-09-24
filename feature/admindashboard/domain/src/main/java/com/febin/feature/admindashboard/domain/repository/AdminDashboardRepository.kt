package com.febin.feature.admindashboard.domain.repository

import com.febin.feature.admindashboard.domain.model.AdminMetrics
import com.febin.shared_domain.model.Result

/**
 * Domain interface for Admin Dashboard operations.
 * - Abstracts data sources; returns Result<AdminMetrics>.
 */
interface AdminDashboardRepository {
    /**
     * Fetches admin dashboard metrics.
     * @return Result<AdminMetrics>: Success with aggregated admin data.
     */
    suspend fun getAdminMetrics(): Result<AdminMetrics>

    /**
     * Resolves a report.
     * @param reportId The report ID.
     * @return Result<Unit>.
     */
    suspend fun resolveReport(reportId: String): Result<Unit>

    /**
     * Refreshes admin data.
     * @return Result<AdminMetrics>.
     */
    suspend fun refreshAdminMetrics(): Result<AdminMetrics>
}