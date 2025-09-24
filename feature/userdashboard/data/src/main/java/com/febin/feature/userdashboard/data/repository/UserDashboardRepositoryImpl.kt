package com.febin.feature.userdashboard.data.repository

import com.febin.feature.userdashboard.data.mapper.DashboardMapper
import com.febin.feature.userdashboard.data.remote.api.UserDashboardApi
import com.febin.feature.dashboard.domain.model.ActivityItem  // Added: Import for ActivityItem fields
import com.febin.feature.dashboard.domain.model.DashboardData  // Fixed: Correct package (userdashboard, not dashboard)
import com.febin.feature.dashboard.domain.model.UserMetrics  // Added for toDomain(localDashboard)
import com.febin.feature.dashboard.domain.repository.UserDashboardRepository
import com.febin.feature.userdashboard.data.local.dao.DashboardDao
import com.febin.feature.userdashboard.data.local.entity.DashboardEntity
import com.febin.feature.userdashboard.data.local.entity.ActivityEntity
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.User
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
                // Reconstruct from local (simplified; fetch activities)
                val activities = dao.getRecentActivities(userId).first().map { activityEntity ->
                    // Map ActivityEntity to ActivityItem (add to mapper if needed)
                    ActivityItem(
                        id = activityEntity.id,
                        title = activityEntity.title,
                        description = activityEntity.description,
                        timestamp = activityEntity.timestamp
                    )
                }
                val metrics = UserMetrics(
                    totalLogins = localDashboard.totalLogins,
                    lastLogin = localDashboard.lastLogin,
                    points = localDashboard.points,
                    level = localDashboard.level
                )
                val data = DashboardData(
                    user = User(id = userId, email = ""),  // From auth or local
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
        // Cache metrics
        dao.insertDashboard(
            DashboardEntity(
                userId = userId,
                totalLogins = data.metrics.totalLogins,
                lastLogin = data.metrics.lastLogin,
                points = data.metrics.points,
                level = data.metrics.level,
                notifications = data.notifications
            )
        )
        // Cache activities
        dao.insertActivities(
            data.recentActivity.map { activity ->  // activity is ActivityItem
                ActivityEntity(
                    id = activity.id,  // Now resolves with import
                    userId = userId,
                    title = activity.title,
                    description = activity.description,
                    timestamp = activity.timestamp
                )
            }
        )
    }
}