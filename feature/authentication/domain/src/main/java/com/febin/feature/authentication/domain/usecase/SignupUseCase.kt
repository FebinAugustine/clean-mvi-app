package com.febin.feature.authentication.domain.usecase



import com.febin.feature.authentication.domain.model.SignupError
import com.febin.feature.authentication.domain.repository.AuthRepository
import com.febin.shared_domain.model.Result
import com.febin.shared_domain.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Use case for user signup.
 * - Similar to LoginUseCase: Validation + repo delegation.
 * - Returns Flow<Result<User>> for MVI.
 */
class SignupUseCase(
    private val authRepository: AuthRepository
) {
    /**
     * Invokes the signup flow.
     * @param fullName User's full name.
     * @param email User's email.
     * @param password User's password.
     * @param phone User's phone.
     * @return Flow<Result<User>>.
     */
    operator fun invoke(
        fullName: String,
        email: String,
        password: String,
        phone: String
    ): Flow<Result<User>> = flow {
        // Domain validation
        if (fullName.isBlank() || email.isBlank() || password.isBlank() || phone.isBlank()) {
            emit(Result.failure(SignupError.MissingRequiredField("All fields are required.")))
            return@flow
        }
        if (!email.isValidEmail()) {
            emit(Result.failure(SignupError.MissingRequiredField("Invalid email.")))
            return@flow
        }
        if (!password.isValidPassword()) {
            emit(Result.failure(SignupError.MissingRequiredField("Weak password.")))
            return@flow
        }
        if (!phone.isValidPhone()) {  // Simple check
            emit(Result.failure(SignupError.InvalidPhone))
            return@flow
        }

        emit(Result.loading())

        try {
            val result = authRepository.signup(fullName, email, password, phone)
            when (result) {
                is Result.Success -> emit(Result.success(result.data))
                is Result.Failure -> {
                    val error = when {
                        result.exception.message?.contains("409") == true -> SignupError.EmailAlreadyExists
                        result.exception.message?.contains("network") == true -> SignupError.MissingRequiredField("Network error.")
                        else -> SignupError.MissingRequiredField(result.exception.message ?: "Signup failed.")
                    }
                    emit(Result.failure(error))
                }
                else -> {}
            }
        } catch (e: Exception) {
            emit(Result.failure(SignupError.MissingRequiredField(e.message ?: "Unexpected error.")))
        }
    }
}

private fun String.isValidPhone(): Boolean = this.matches(Regex("^[0-9+\\-\\s()]{10,15}$"))  // Basic phone regex