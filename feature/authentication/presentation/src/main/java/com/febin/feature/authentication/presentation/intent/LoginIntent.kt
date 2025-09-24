package com.febin.feature.authentication.presentation.intent


/**
 * Sealed class for Login MVI intents (user actions).
 * - Sent from UI to ViewModel via Channel.
 */
sealed class LoginIntent {
    data class EnterEmail(val email: String) : LoginIntent()
    data class EnterPassword(val password: String) : LoginIntent()
    object Submit : LoginIntent()
    object ClearError : LoginIntent()  // Optional: Dismiss error
}