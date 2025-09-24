// Updated: feature/authentication/domain/model/LoginError.kt
package com.febin.feature.authentication.domain.model

import com.febin.shared_domain.model.UserError  // Now extends abstract UserError

/**
 * Sealed class for authentication-specific errors.
 * - Domain-focused: No HTTP codes; map from data layer.
 * - Extends shared UserError (abstract, so cross-module OK).
 */
sealed class LoginError : UserError() {
    data object InvalidCredentials : LoginError() {
        override fun getUserMessage(): String = "Invalid email or password."
    }

    data object AccountNotVerified : LoginError() {
        private fun readResolve(): Any = AccountNotVerified
        override fun getUserMessage(): String = "Please verify your account."
    }

    data class WeakPassword(val requiredLength: Int) : LoginError() {
        override fun getUserMessage(): String = "Password must be at least $requiredLength characters."
    }

    data object NetworkUnavailable : LoginError() {
        private fun readResolve(): Any = NetworkUnavailable
        override fun getUserMessage(): String = "No internet connection."
    }

    data class Unknown(override val message: String) : LoginError()

    // Convenience for MVI state (override from base if needed)
    override fun getUserMessage(): String = when (this) {
        InvalidCredentials -> "Invalid email or password."
        AccountNotVerified -> "Please verify your account."
        is WeakPassword -> "Password must be at least $requiredLength characters."
        NetworkUnavailable -> "No internet connection."
        is Unknown -> message
    }
}

/**
 * Similar for signup errors (can merge if simple).
 */
sealed class SignupError : UserError() {
    data object EmailAlreadyExists : SignupError() {
        private fun readResolve(): Any = EmailAlreadyExists
        override fun getUserMessage(): String = "Email already exists."
    }

    data object InvalidPhone : SignupError() {
        private fun readResolve(): Any = InvalidPhone
        override fun getUserMessage(): String = "Invalid phone number."
    }

    data class MissingRequiredField(val field: String) : SignupError() {
        override fun getUserMessage(): String = "Missing required field: $field"
    }

    data object TermsNotAccepted : SignupError() {
        private fun readResolve(): Any = TermsNotAccepted
        override fun getUserMessage(): String = "Terms must be accepted."
    }

    // Convenience method
    override fun getUserMessage(): String = when (this) {
        EmailAlreadyExists -> "Email already exists."
        InvalidPhone -> "Invalid phone number."
        is MissingRequiredField -> "Missing required field: $field"
        TermsNotAccepted -> "Terms must be accepted."
    }
}