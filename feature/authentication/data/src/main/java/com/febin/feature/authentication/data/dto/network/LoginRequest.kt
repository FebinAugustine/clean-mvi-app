package com.febin.feature.authentication.data.dto.network



import kotlinx.serialization.Serializable

/**
 * DTO for login request body (JSON serializable for Ktor).
 * - Matches API schema; mapped to domain in repo.
 */
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)