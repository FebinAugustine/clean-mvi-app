package com.febin.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.febin.core.data.local.entity.TokenEntity
import com.febin.core.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

/**
 * Room DAO for auth caching.
 * - Entities: UserEntity, TokenEntity.
 */
@Dao
interface AuthDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE id = :userId")
    fun getUserById(userId: String): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToken(token: TokenEntity)

    @Query("SELECT * FROM tokens LIMIT 1")
    fun getCurrentToken(): Flow<TokenEntity?>

    @Query("DELETE FROM users")
    suspend fun clearUsers()

    @Query("DELETE FROM tokens")
    suspend fun clearTokens()
}