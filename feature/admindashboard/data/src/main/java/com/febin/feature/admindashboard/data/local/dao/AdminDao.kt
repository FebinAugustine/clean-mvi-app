package com.febin.feature.admindashboard.data.local.dao


import androidx.room.*
import com.febin.feature.admindashboard.data.local.entity.AdminMetricsEntity
import com.febin.feature.admindashboard.data.local.entity.ReportEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for admin caching.
 */
@Dao
interface AdminDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAdminMetrics(metrics: AdminMetricsEntity)

    @Query("SELECT * FROM admin_metrics LIMIT 1")
    fun getAdminMetrics(): Flow<AdminMetricsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReports(reports: List<ReportEntity>)

    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getPendingReports(): Flow<List<ReportEntity>>

    @Query("DELETE FROM admin_metrics")
    suspend fun clearMetrics()

    @Query("DELETE FROM reports")
    suspend fun clearReports()
}