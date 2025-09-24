package com.febin.feature.authentication.presentation.state



import com.febin.feature.authentication.domain.model.LoginError
import com.febin.feature.authentication.domain.usecase.isValidEmail
import com.febin.feature.authentication.domain.usecase.isValidPassword
import com.febin.shared_domain.model.User


/**
 * Data class for Login MVI state.
 * - Immutable; updated via StateFlow in ViewModel.
 * - Includes validation errors from domain.
 */
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: LoginError? = null,  // Domain error
    val user: User? = null  // On success
) {
    val isEmailValid: Boolean = email.isValidEmail()  // From core util
    val isPasswordValid: Boolean = password.isValidPassword()
    val canSubmit: Boolean = isEmailValid && isPasswordValid && !isLoading
}