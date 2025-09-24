package com.febin.feature.authentication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.febin.feature.authentication.domain.model.AuthToken  // Import for fromDomain param

@Entity(tableName = "tokens")
data class TokenEntity(
    @PrimaryKey val id: String = "current",  // Single token
    val token: String,
    val expiresIn: Long?,
    val refreshToken: String?,  // Ensured field is present
    val type: String = "BEARER"
) {
    companion object {
        fun fromDomain(token: AuthToken): TokenEntity = TokenEntity(
            token = token.token,
            expiresIn = token.expiresIn,
            refreshToken = token.refreshToken,  // Now resolves
            type = token.type.name
        )
    }
}