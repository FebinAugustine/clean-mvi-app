package com.febin.feature.authentication.data.remote.datasource

import com.febin.feature.authentication.data.dto.network.LoginRequest
import com.febin.feature.authentication.data.dto.network.SignupResponseDto
import com.febin.feature.authentication.data.dto.network.TokenDto
import com.febin.feature.authentication.data.remote.api.AuthApi
import com.febin.feature.authentication.data.remote.api.SignupRequest
import com.febin.shared_domain.model.Result

/**
 * Remote data source for auth operations.
 * - Delegates to API; wraps in Result for consistency.
 */
class RemoteAuthDataSource(
    private val authApi: AuthApi
) {
    suspend fun login(email: String, password: String): Result<SignupResponseDto> {  // Placeholder; map in repo
        return try {
            val request = LoginRequest(email, password)
            val response = authApi.login(request)
            Result.success(response)  // DTO
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signup(fullName: String, email: String, password: String, phone: String): Result<SignupResponseDto> {  // Fixed: Return SignupResponseDto for consistency
        return try {
            val request = SignupRequest(fullName, email, password, phone)
            val response = authApi.signup(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Similar for refresh, logout (e.g., clear token)
    suspend fun refreshToken(refreshToken: String): Result<TokenDto> = try {
        // Implement API call (e.g., authApi.refresh(refreshToken))
        val response = authApi.refreshToken(refreshToken)  // Assume AuthApi has this
        Result.success(response)
    } catch (e: Exception) {
        Result.failure(e)
    }
}