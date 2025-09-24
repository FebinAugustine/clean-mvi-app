package com.febin.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters  // Fixed: Use TypeConverters, not RoomTypeConverters
import com.febin.feature.authentication.data.local.dao.AuthDao
import com.febin.feature.authentication.data.local.entity.TokenEntity
import com.febin.feature.authentication.data.local.entity.UserEntity
import com.febin.feature.userdashboard.data.local.dao.DashboardDao
import com.febin.feature.userdashboard.data.local.entity.ActivityEntity
import com.febin.feature.userdashboard.data.local.entity.DashboardEntity
import com.febin.feature.admindashboard.data.local.dao.AdminDao
import com.febin.feature.admindashboard.data.local.entity.AdminMetricsEntity
import com.febin.feature.admindashboard.data.local.entity.ReportEntity

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
@TypeConverters(Converters::class)  // Fixed: Use TypeConverters annotation
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun adminDao(): AdminDao
}