package com.febin.feature.authentication.data.local.datasource

import com.febin.core.data.local.dao.AuthDao
import com.febin.core.data.local.entity.TokenEntity
import com.febin.core.data.local.entity.UserEntity
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.getOrNull
import kotlinx.coroutines.flow.firstOrNull

/**
 * Local data source for auth (Room caching).
 * - Simple CRUD; used for offline/ persistence.
 */
class LocalAuthDataSource(
    private val authDao: AuthDao
) {
    suspend fun saveUser(user: UserEntity): Result<Unit> = try {
        authDao.insertUser(user)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getUser(userId: String): Result<UserEntity?> = try {
        Result.success(authDao.getUserById(userId).firstOrNull())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun saveToken(token: TokenEntity): Result<Unit> = try {
        authDao.insertToken(token)
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun getToken(): Result<TokenEntity?> = try {
        Result.success(authDao.getCurrentToken().firstOrNull())
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun clearAuth(): Result<Unit> = try {
        authDao.clearUsers()
        authDao.clearTokens()
        Result.success(Unit)
    } catch (e: Exception) {
        Result.failure(e)
    }

    suspend fun isAuthenticated(): Boolean = try {
        val tokenResult = getToken()
        tokenResult.getOrNull() != null
    } catch (e: Exception) {
        false
    }
}