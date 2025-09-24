package com.febin.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.febin.core.data.local.dao.AdminDao
import com.febin.core.data.local.dao.AuthDao
import com.febin.core.data.local.dao.DashboardDao
import com.febin.core.data.local.entity.ActivityEntity
import com.febin.core.data.local.entity.AdminMetricsEntity
import com.febin.core.data.local.entity.DashboardEntity
import com.febin.core.data.local.entity.ReportEntity
import com.febin.core.data.local.entity.TokenEntity
import com.febin.core.data.local.entity.UserEntity

/**
 * Global Room database for the app.
 * - Includes all entities from features (auth, user, admin dashboards).
 * - TypeConverters for custom types (e.g., enums if needed).
 */
@Database(
    entities = [
        UserEntity::class, TokenEntity::class,
        DashboardEntity::class, ActivityEntity::class,
        AdminMetricsEntity::class, ReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun adminDao(): AdminDao
}