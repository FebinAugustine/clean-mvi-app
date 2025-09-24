package com.febin.feature.admindashboard.data.dto.network

import kotlinx.serialization.Serializable

/**
 * DTO for admin dashboard response.
 */
@Serializable
data class AdminResponseDto(
    val totalUsers: Int,
    val activeUsers: Int,
    val pendingReports: List<ReportItemDto>,
    val recentAdmins: List<AdminUserDto>,
    val systemUptime: String?
)

/**
 * Nested DTOs.
 */
@Serializable
data class ReportItemDto(
    val id: String,
    val title: String,
    val description: String,
    val status: String,  // "PENDING", etc.
    val timestamp: String
)

@Serializable
data class AdminUserDto(
    val id: String,
    val email: String,
    val name: String?
)