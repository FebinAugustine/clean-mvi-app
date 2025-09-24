package com.febin.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tokens")
data class TokenEntity(
    @PrimaryKey val id: String = "current",
    val token: String,
    val expiresIn: Long?,
    val refreshToken: String?,
    val type: String = "BEARER"
)
