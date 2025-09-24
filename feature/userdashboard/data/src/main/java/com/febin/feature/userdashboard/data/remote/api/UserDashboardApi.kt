package com.febin.feature.userdashboard.data.remote.api

import com.febin.feature.userdashboard.data.dto.network.UserDashboardResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import kotlinx.serialization.Serializable

/**
 * Interface for User Dashboard API via Ktor.
 */
interface UserDashboardApi {
    suspend fun getDashboardData(userId: String): UserDashboardResponseDto
}

class UserDashboardApiImpl(
    private val client: HttpClient
) : UserDashboardApi {
    private val baseUrl = "https://api.example.com/dashboard"

    override suspend fun getDashboardData(userId: String): UserDashboardResponseDto {
        return try {
            client.get(URLBuilder(baseUrl).appendPathSegments("user").build()) {
                parameter("userId", userId)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }
}