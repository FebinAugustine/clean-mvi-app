package com.febin.feature.admindashboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "admin_metrics")
data class AdminMetricsEntity(
    @PrimaryKey val id: String = "admin_metrics",
    val totalUsers: Int,
    val activeUsers: Int,
    val systemUptime: String?
)