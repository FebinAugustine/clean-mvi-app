// Updated: feature/authentication/data/repository/AuthRepositoryImpl.kt
package com.febin.feature.authentication.data.repository

import com.febin.feature.authentication.data.dto.network.SignupResponseDto
import com.febin.feature.authentication.data.dto.network.TokenDto
import com.febin.feature.authentication.data.mapper.AuthMapper
import com.febin.feature.authentication.data.remote.datasource.RemoteAuthDataSource
import com.febin.feature.authentication.data.local.datasource.LocalAuthDataSource
import com.febin.feature.authentication.data.local.entity.TokenEntity
import com.febin.feature.authentication.data.local.entity.UserEntity
import com.febin.feature.authentication.domain.model.AuthToken
import com.febin.feature.authentication.domain.repository.AuthRepository
import com.febin.feature.authentication.domain.model.LoginError
import com.febin.feature.authentication.domain.model.SignupError
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.SharedUserError
import com.febin.shared_domain.model.User
import com.febin.shared_domain.model.exceptionOrNull
import com.febin.shared_domain.model.getOrNull
import com.febin.shared_domain.model.isSuccess
import com.febin.shared_domain.usecase.*  // Imports extensions (isSuccess, getOrNull, exceptionOrNull)

/**
 * Implementation of AuthRepository.
 * - Combines remote/local: Try local first for auth check; remote for login/signup, cache on success.
 * - Maps DTOs/entities to domain; errors to domain types.
 */
class AuthRepositoryImpl(
    private val remoteDataSource: RemoteAuthDataSource,
    private val localDataSource: LocalAuthDataSource
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val remoteResult = remoteDataSource.login(email, password)
            when {
                remoteResult.isSuccess() -> {
                    val response = remoteResult.getOrNull() as? SignupResponseDto
                        ?: return Result.failure(LoginError.Unknown("Invalid response"))
                    val domainUser = AuthMapper.toDomain(response.user)
                    val domainToken = AuthMapper.toDomain(response.token)

                    // Cache locally
                    localDataSource.saveUser(UserEntity.fromDomain(domainUser))
                    localDataSource.saveToken(TokenEntity.fromDomain(domainToken))

                    Result.success(domainUser)
                }
                else -> Result.failure(AuthMapper.mapToLoginError(remoteResult.exceptionOrNull() ?: Throwable("Unknown error")))
            }
        } catch (e: Exception) {
            Result.failure(AuthMapper.mapToLoginError(e))
        }
    }

    override suspend fun signup(
        fullName: String,
        email: String,
        password: String,
        phone: String
    ): Result<User> {
        return try {
            val remoteResult = remoteDataSource.signup(fullName, email, password, phone)
            when {
                remoteResult.isSuccess() -> {
                    val response = remoteResult.getOrNull() as? SignupResponseDto
                        ?: return Result.failure(SignupError.MissingRequiredField("Invalid response"))
                    val domainUser = AuthMapper.toDomain(response.user)
                    val domainToken = AuthMapper.toDomain(response.token)

                    // Cache
                    localDataSource.saveUser(UserEntity.fromDomain(domainUser))
                    localDataSource.saveToken(TokenEntity.fromDomain(domainToken))

                    Result.success(domainUser)
                }
                else -> Result.failure(AuthMapper.mapToSignupError(remoteResult.exceptionOrNull() ?: Throwable("Unknown error")))
            }
        } catch (e: Exception) {
            Result.failure(AuthMapper.mapToSignupError(e))
        }
    }

    override suspend fun refreshToken(refreshToken: String): Result<AuthToken> {
        return try {
            val remoteResult = remoteDataSource.refreshToken(refreshToken)
            when {
                remoteResult.isSuccess() -> {
                    val domainToken = AuthMapper.toDomain(remoteResult.getOrNull() as? TokenDto
                        ?: return Result.failure(SharedUserError.Unknown("Invalid token DTO")))  // Fixed: Use Unknown with message
                    localDataSource.saveToken(TokenEntity.fromDomain(domainToken))
                    Result.success(domainToken)
                }
                else -> Result.failure(SharedUserError.Unknown("Refresh failed"))  // Fixed: Use Unknown with message
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> = localDataSource.clearAuth()

    override suspend fun isAuthenticated(): Boolean = localDataSource.isAuthenticated()
}