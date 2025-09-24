package com.febin.feature.authentication.presentation.state



import com.febin.feature.authentication.domain.model.SignupError
import com.febin.feature.authentication.domain.usecase.isValidEmail
import com.febin.feature.authentication.domain.usecase.isValidPassword
import com.febin.shared.domain.model.User
import com.febin.shared_domain.model.User

/**
 * Data class for Signup MVI state.
 * - Similar to LoginState; more fields.
 */
data class SignupState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val isLoading: Boolean = false,
    val error: SignupError? = null,
    val user: User? = null
) {
    val isEmailValid: Boolean = email.isValidEmail()
    val isPasswordValid: Boolean = password.isValidPassword()
    val isPhoneValid: Boolean = phone.isValidPhone()  // From domain util
    val canSubmit: Boolean = fullName.isNotBlank() && isEmailValid && isPasswordValid && isPhoneValid && !isLoading
}