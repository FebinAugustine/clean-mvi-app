// Updated: shared_domain/usecase/GetSharedUserUseCase.kt (remove duplication; import Result)
package com.febin.shared_domain.usecase

import com.febin.shared_domain.model.Result  // Import Result from model
import com.febin.shared_domain.model.SharedUserError  // For specific errors
import com.febin.shared_domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Shared use case for fetching a User by ID.
 * - Pure domain: Depends on a repository interface.
 * - Returns Flow<Result<User>> for async, observable results (MVI-friendly).
 * - Cross-feature: Used in auth (post-login) or dashboard (profile load).
 */
class GetSharedUserUseCase(
    private val userRepository: UserRepository  // Injected via DI (e.g., Koin)
) {
    /**
     * Invokes the use case: Get user by ID.
     * @param userId The unique user identifier.
     * @return Flow<Result<User>>: Emits loading, success, or failure.
     */
    operator fun invoke(userId: String): Flow<Result<User>> = flow {
        emit(Result.loading())  // Optional: Emit loading state

        try {
            val user = userRepository.getUserById(userId)
            if (user != null) {
                emit(Result.success(user))
            } else {
                emit(Result.failure(SharedUserError.NotFound))  // Fixed: Use SharedUserError.NotFound
            }
        } catch (e: Exception) {
            // Map to domain error if needed
            emit(Result.failure(SharedUserError.InvalidEmail("Invalid user ID: $userId")))  // Fixed: Use SharedUserError.InvalidEmail
        }
    }
}

/**
 * Simple repository interface for User operations.
 * - Defined here for shared use; implemented in feature :data modules.
 * - Keeps domain pure: No impl details.
 */
interface UserRepository {
    /**
     * Fetches a user by ID from any data source (remote/local).
     * @param id The user ID.
     * @return User? or null if not found.
     */
    suspend fun getUserById(id: String): User?
}