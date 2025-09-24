package com.febin.di.database


import androidx.room.Database
import androidx.room.RoomDatabase
import com.febin.feature.authentication.data.local.dao.AuthDao
import com.febin.feature.userdashboard.data.local.dao.DashboardDao
import com.febin.feature.admindashboard.data.local.dao.AdminDao
import com.febin.feature.authentication.data.local.entity.UserEntity
import com.febin.feature.authentication.data.local.entity.TokenEntity
import com.febin.feature.userdashboard.data.local.entity.DashboardEntity
import com.febin.feature.userdashboard.data.local.entity.ActivityEntity
import com.febin.feature.admindashboard.data.local.entity.AdminMetricsEntity
import com.febin.feature.admindashboard.data.local.entity.ReportEntity

@Database(
    entities = [
        UserEntity::class, TokenEntity::class,
        DashboardEntity::class, ActivityEntity::class,
        AdminMetricsEntity::class, ReportEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun authDao(): AuthDao
    abstract fun dashboardDao(): DashboardDao
    abstract fun adminDao(): AdminDao
}