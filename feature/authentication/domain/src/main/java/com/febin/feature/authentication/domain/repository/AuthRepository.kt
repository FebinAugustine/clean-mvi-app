package com.febin.feature.authentication.domain.repository

import com.febin.feature.authentication.domain.model.AuthToken
import com.febin.feature.authentication.domain.model.LoginError
import com.febin.feature.authentication.domain.model.SignupError
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.User

/**
 * Domain interface for authentication operations.
 * - Abstracts data sources (remote/local); implemented in :data.
 * - Returns Result<User> for success/failure; errors mapped to domain types.
 */
interface AuthRepository {
    /**
     * Performs user login.
     * @param email User's email.
     * @param password User's password.
     * @return Result<User> with optional AuthToken on success.
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Performs user signup/registration.
     * @param fullName User's full name.
     * @param email User's email.
     * @param password User's password.
     * @param phone User's phone number.
     * @return Result<User> with AuthToken on success.
     */
    suspend fun signup(
        fullName: String,
        email: String,
        password: String,
        phone: String
    ): Result<User>

    /**
     * Refreshes the auth token.
     * @param refreshToken The refresh token.
     * @return Result<AuthToken>.
     */
    suspend fun refreshToken(refreshToken: String): Result<AuthToken>

    /**
     * Logs out the user (e.g., clear token from local storage).
     * @return Result<Unit> for success.
     */
    suspend fun logout(): Result<Unit>

    /**
     * Checks if user is currently authenticated.
     * @return Boolean based on valid token.
     */
    suspend fun isAuthenticated(): Boolean
}