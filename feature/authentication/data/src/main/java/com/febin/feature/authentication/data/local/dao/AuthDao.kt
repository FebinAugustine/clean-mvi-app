package com.febin.feature.authentication.data.local.dao



import androidx.room.*
import com.febin.feature.authentication.data.local.entity.UserEntity
import com.febin.feature.authentication.data.local.entity.TokenEntity
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