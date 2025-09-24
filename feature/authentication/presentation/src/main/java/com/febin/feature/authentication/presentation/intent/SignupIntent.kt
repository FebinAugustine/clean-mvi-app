package com.febin.feature.authentication.presentation.intent



/**
 * Sealed class for Signup MVI intents.
 * - Similar to Login; additional fields.
 */
sealed class SignupIntent {
    data class EnterFullName(val fullName: String) : SignupIntent()
    data class EnterEmail(val email: String) : SignupIntent()
    data class EnterPassword(val password: String) : SignupIntent()
    data class EnterPhone(val phone: String) : SignupIntent()
    object Submit : SignupIntent()
    object ClearError : SignupIntent()
}