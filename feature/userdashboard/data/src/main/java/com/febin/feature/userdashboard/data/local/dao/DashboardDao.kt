package com.febin.feature.userdashboard.data.local.dao

import androidx.room.*
import com.febin.feature.userdashboard.data.local.entity.DashboardEntity
import com.febin.feature.userdashboard.data.local.entity.ActivityEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for dashboard caching.
 */
@Dao
interface DashboardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDashboard(dashboard: DashboardEntity)

    @Query("SELECT * FROM dashboards WHERE userId = :userId")
    fun getDashboardByUserId(userId: String): Flow<DashboardEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivities(activities: List<ActivityEntity>)

    @Query("SELECT * FROM activities WHERE userId = :userId ORDER BY timestamp DESC LIMIT 10")
    fun getRecentActivities(userId: String): Flow<List<ActivityEntity>>

    @Query("DELETE FROM dashboards WHERE userId = :userId")
    suspend fun clearDashboard(userId: String)

    @Query("DELETE FROM activities WHERE userId = :userId")
    suspend fun clearActivities(userId: String)
}