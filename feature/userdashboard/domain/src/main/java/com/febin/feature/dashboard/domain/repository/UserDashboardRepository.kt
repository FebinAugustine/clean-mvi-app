package com.febin.feature.dashboard.domain.repository

import com.febin.feature.dashboard.domain.model.DashboardData
import com.febin.shared_domain.model.Result

/**
 * Domain interface for User Dashboard operations.
 * - Abstracts data sources; returns Result<DashboardData>.
 */
interface UserDashboardRepository {
    /**
     * Fetches dashboard data for the current user.
     * @param userId The user's ID (from auth).
     * @return Result<DashboardData>: Success with aggregated data.
     */
    suspend fun getDashboardData(userId: String): Result<DashboardData>

    /**
     * Refreshes dashboard data (e.g., pull-to-refresh).
     * @param userId The user's ID.
     * @return Result<DashboardData>.
     */
    suspend fun refreshDashboardData(userId: String): Result<DashboardData>

    /**
     * Marks notifications as read.
     * @return Result<Unit>.
     */
    suspend fun markNotificationsRead(): Result<Unit>
}