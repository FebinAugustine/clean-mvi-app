package com.febin.feature.admindashboard.data.dto.network

import kotlinx.serialization.Serializable

@Serializable
data class AdminResponseDto(
    val metrics: AdminMetricsDto,
    val reports: List<ReportDto>,
    val admins: List<AdminUserDto>
)

@Serializable
data class AdminMetricsDto(
    val totalUsers: Int,
    val activeUsers: Int,
    val systemUptime: String?
)

@Serializable
data class ReportDto(
    val id: String,
    val title: String,
    val description: String,
    val status: String,
    val timestamp: String
)

@Serializable
data class AdminUserDto(
    val id: String,
    val name: String,
    val email: String
)