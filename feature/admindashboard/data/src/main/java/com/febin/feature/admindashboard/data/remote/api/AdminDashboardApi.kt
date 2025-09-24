package com.febin.feature.admindashboard.data.remote.api

import com.febin.feature.admindashboard.data.dto.network.AdminResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments

/**
 * Interface for Admin Dashboard API via Ktor.
 */
interface AdminDashboardApi {
    suspend fun getAdminMetrics(): AdminResponseDto
}

class AdminDashboardApiImpl(
    private val client: HttpClient
) : AdminDashboardApi {
    private val baseUrl = "https://api.example.com/admin"

    override suspend fun getAdminMetrics(): AdminResponseDto {
        return try {
            client.get(URLBuilder(baseUrl).appendPathSegments("dashboard").build()).body()
        } catch (e: Exception) {
            throw e
        }
    }
}