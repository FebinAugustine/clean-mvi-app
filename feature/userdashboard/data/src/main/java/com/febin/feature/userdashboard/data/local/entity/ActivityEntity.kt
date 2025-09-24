package com.febin.feature.userdashboard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activities")
data class ActivityEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val title: String,
    val description: String,
    val timestamp: String
)