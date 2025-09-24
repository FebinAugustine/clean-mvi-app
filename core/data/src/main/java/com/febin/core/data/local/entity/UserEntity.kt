package com.febin.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String?,
    val phone: String?,
    val role: String = "USER",
    val isVerified: Boolean = false
)
