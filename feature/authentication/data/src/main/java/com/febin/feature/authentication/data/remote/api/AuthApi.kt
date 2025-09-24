package com.febin.feature.authentication.data.remote.api


import com.febin.feature.authentication.data.dto.network.LoginRequest
import com.febin.feature.authentication.data.dto.network.SignupResponseDto
import com.febin.feature.authentication.data.dto.network.TokenDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import kotlinx.serialization.Serializable

/**
 * Interface for Auth API calls via Ktor.
 * - Suspend functions for coroutines.
 * - Returns DTOs; errors via exceptions (mapped in repo).
 */
interface AuthApi {
    suspend fun login(request: LoginRequest): SignupResponseDto  // Reuse for login response
    suspend fun signup(request: SignupRequest): SignupResponseDto
    suspend fun refreshToken(refreshToken: String): TokenDto
}

class AuthApiImpl(
    private val client: HttpClient
) : AuthApi {
    private val baseUrl = "https://api.example.com/auth"  // From constants if shared

    override suspend fun login(request: LoginRequest): SignupResponseDto {
        return try {
            client.post(URLBuilder(baseUrl).appendPathSegments("login").build()) {
                setBody(request)
            }.body()
        } catch (e: Exception) {
            throw e  // Let repo handle/map
        }
    }

    override suspend fun signup(request: SignupRequest): SignupResponseDto {
        return try {
            client.post(URLBuilder(baseUrl).appendPathSegments("signup").build()) {
                setBody(request)
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun refreshToken(refreshToken: String): TokenDto {
        return try {
            client.post(URLBuilder(baseUrl).appendPathSegments("refresh").build()) {
                // Add refreshToken as param or body
            }.body()
        } catch (e: Exception) {
            throw e
        }
    }
}

/**
 * DTO for signup request (similar to LoginRequest).
 */

@Serializable
data class SignupRequest(
    val fullName: String,
    val email: String,
    val password: String,
    val phone: String
)