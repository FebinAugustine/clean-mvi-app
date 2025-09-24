package com.febin.feature.authentication.domain.model

/**
 * Domain model for authentication token.
 * - Immutable; used post-login/signup for session management.
 * - Shared across features if needed, but feature-specific here.
 */
data class AuthToken(
    val token: String,
    val expiresIn: Long? = null,  // In seconds
    val refreshToken: String? = null,
    val type: TokenType = TokenType.BEARER
)

/**
 * Enum for token types.
 */
enum class TokenType {
    BEARER,
    JWT;

    fun getPrefix(): String = "Bearer "
}