package com.febin.feature.userdashboard.data.repository

import com.febin.core.data.local.dao.DashboardDao
import com.febin.feature.dashboard.domain.model.DashboardData
import com.febin.feature.dashboard.domain.repository.UserDashboardRepository
import com.febin.feature.userdashboard.data.mapper.DashboardMapper
import com.febin.feature.userdashboard.data.remote.api.UserDashboardApi
import com.febin.shared_domain.model.Result
import kotlinx.coroutines.flow.first

/**
 * Impl of UserDashboardRepository.
 * - Cache-first: Local query, fallback to remote + cache.
 */
class UserDashboardRepositoryImpl(
    private val api: UserDashboardApi,
    private val dao: DashboardDao
) : UserDashboardRepository {

    override suspend fun getDashboardData(userId: String): Result<DashboardData> {
        return try {
            // Try local first
            val localDashboard = dao.getDashboardByUserId(userId).first()
            if (localDashboard != null) {
                val activities = dao.getRecentActivities(userId).first().map { activityEntity ->
                    DashboardMapper.toDomain(activityEntity)
                }
                val metrics = com.febin.feature.dashboard.domain.model.UserMetrics(
                    totalLogins = localDashboard.totalLogins,
                    lastLogin = localDashboard.lastLogin,
                    points = localDashboard.points,
                    level = localDashboard.level
                )
                val data = DashboardData(
                    user = com.febin.shared_domain.model.User(id = userId, email = ""),  // From auth or local
                    metrics = metrics,
                    recentActivity = activities,
                    notifications = localDashboard.notifications
                )
                return Result.success(data)
            }

            // Fallback to remote
            val response = api.getDashboardData(userId)
            val domainData = DashboardMapper.toDomain(response)

            // Cache
            cacheDashboard(domainData, userId)

            Result.success(domainData)
        } catch (e: Exception) {
            Result.failure(DashboardMapper.mapToDashboardError(e))
        }
    }

    override suspend fun refreshDashboardData(userId: String): Result<DashboardData> {
        return getDashboardData(userId)  // Reuse; force remote in prod by clearing cache first
    }

    override suspend fun markNotificationsRead(): Result<Unit> = try {
        // Update local
        dao.getDashboardByUserId("current").first()?.let { dashboard ->
            dao.insertDashboard(dashboard.copy(notifications = 0))
        }
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    private suspend fun cacheDashboard(data: DashboardData, userId: String) {
        dao.insertDashboard(DashboardMapper.fromDomain(data, userId))
        dao.insertActivities(data.recentActivity.map { DashboardMapper.fromDomain(it, userId) })
    }
}