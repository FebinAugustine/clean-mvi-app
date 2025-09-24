package com.febin.shared_domain.usecase

import com.febin.shared_domain.model.SharedUserError  // Fixed: Use SharedUserError for specific errors
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

/**
 * Extension for Result (Kotlin stdlib) to add loading state.
 * - Custom for domain; use sealed Result or Either in prod for richer errors.
 */
sealed class Result<out T> {
    data object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Failure(val exception: Throwable) : Result<Nothing>()

    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Loading -> Loading
        is Success -> Success(transform(data))
        is Failure -> Failure(exception)
    }

    companion object {
        fun <T> loading(): Result<T> = Loading as Result<T>
        fun <T> success(data: T): Result<T> = Success(data)
        fun failure(exception: Throwable): Result<Nothing> = Failure(exception)
    }
}

fun <T> Result<T>.isLoading(): Boolean = this is Result.Loading
fun <T> Result<T>.isSuccess(): Boolean = this is Result.Success
fun <T> Result<T>.getOrNull(): T? = (this as? Result.Success)?.data
fun <T> Result<T>.exceptionOrNull(): Throwable? = (this as? Result.Failure)?.exception