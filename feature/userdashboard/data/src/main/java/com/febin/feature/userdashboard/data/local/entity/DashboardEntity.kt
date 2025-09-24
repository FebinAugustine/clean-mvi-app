package com.febin.feature.userdashboard.data.local.entity


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dashboards")
data class DashboardEntity(
    @PrimaryKey val userId: String,
    val totalLogins: Int,
    val lastLogin: String?,
    val points: Int,
    val level: Int,
    val notifications: Int = 0
)