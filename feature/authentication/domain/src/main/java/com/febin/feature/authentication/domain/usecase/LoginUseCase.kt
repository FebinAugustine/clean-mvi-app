package com.febin.feature.authentication.domain.usecase

import com.febin.feature.authentication.domain.model.LoginError
import com.febin.feature.authentication.domain.repository.AuthRepository
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.User

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for user login.
 * - Delegates to repository; handles domain logic (e.g., validation).
 * - Returns Flow<Result<User>> for MVI: Emits loading → success/failure.
 * - Input validation: Basic email/password checks.
 */
class LoginUseCase(
    private val authRepository: AuthRepository  // Injected via DI
) {
    /**
     * Invokes the login flow.
     * @param email The user's email.
     * @param password The user's password.
     * @return Flow<Result<User>>: Observable for UI state updates.
     */
    operator fun invoke(email: String, password: String): Flow<Result<User>> = flow {
        // Domain validation
        if (email.isBlank() || password.isBlank()) {
            emit(Result.failure(LoginError.Unknown("Email and password are required.")))
            return@flow
        }
        if (!email.isValidEmail()) {
            emit(Result.failure(LoginError.InvalidCredentials))
            return@flow
        }
        if (!password.isValidPassword()) {
            emit(Result.failure(LoginError.WeakPassword(8)))  // From core util
            return@flow
        }

        emit(Result.loading())  // Emit loading state

        try {
            val result = authRepository.login(email, password)
            when (result) {
                is Result.Success -> emit(Result.success(result.data))
                is Result.Failure -> {
                    // Map to domain error (in real: check exception type)
                    val error = when {
                        result.exception.message?.contains("401") == true -> LoginError.InvalidCredentials
                        result.exception.message?.contains("network") == true -> LoginError.NetworkUnavailable
                        else -> LoginError.Unknown(result.exception.message ?: "Login failed.")
                    }
                    emit(Result.failure(error))
                }
                else -> {}  // Loading already emitted
            }
        } catch (e: Exception) {
            emit(Result.failure(LoginError.Unknown(e.message ?: "Unexpected error.")))
        }
    }
}

// Extension from core (assume imported or duplicated for purity)
fun String.isValidEmail(): Boolean = Regex("^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+\\.[A-Za-z]{2,})$").matches(this)  // Pure Kotlin regex for email
fun String.isValidPassword(): Boolean = this.length >= 8