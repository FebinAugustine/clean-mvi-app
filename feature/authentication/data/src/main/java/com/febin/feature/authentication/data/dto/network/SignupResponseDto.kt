package com.febin.feature.authentication.data.dto.network


import kotlinx.serialization.Serializable

/**
 * DTO for signup response from API.
 * - Includes user and token; mapped to domain User/AuthToken.
 */
@Serializable
data class SignupResponseDto(
    val user: UserDto,
    val token: TokenDto
)

/**
 * Nested DTO for user in response.
 */
@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val name: String? = null,
    val phone: String? = null,
    val role: String = "USER"  // String for JSON; map to enum
)

/**
 * Nested DTO for token in response.
 */
@Serializable
data class TokenDto(
    val token: String,
    val expiresIn: Long? = null,
    val refreshToken: String? = null,
    val type: String = "BEARER"  // String for JSON
)