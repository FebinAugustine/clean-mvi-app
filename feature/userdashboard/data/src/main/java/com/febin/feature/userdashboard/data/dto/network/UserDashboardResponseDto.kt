package com.febin.feature.userdashboard.data.dto.network

import kotlinx.serialization.Serializable

/**
 * DTO for dashboard response from API.
 * - Matches JSON schema; mapped to domain.
 */
@Serializable
data class UserDashboardResponseDto(
    val user: UserDto,
    val metrics: MetricsDto,
    val recentActivity: List<ActivityItemDto>,
    val notifications: Int = 0
)

/**
 * Nested DTOs.
 */
@Serializable
data class UserDto(
    val id: String,
    val email: String,
    val name: String? = null
)

@Serializable
data class MetricsDto(
    val totalLogins: Int,
    val lastLogin: String?,
    val points: Int,
    val level: Int
)

@Serializable
data class ActivityItemDto(
    val id: String,
    val title: String,
    val description: String,
    val timestamp: String
)