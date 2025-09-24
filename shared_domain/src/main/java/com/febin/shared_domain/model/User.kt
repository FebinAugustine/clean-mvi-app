// Updated: shared_domain/model/User.kt (or model file)
package com.febin.shared_domain.model

/**
 * Shared domain model for User, reusable across features (e.g., auth, dashboard).
 * Immutable, framework-agnostic.
 */
data class User(
    val id: String,
    val email: String,
    val name: String? = null,
    val phone: String? = null,
    val role: Role = Role.USER,
    val isVerified: Boolean = false
)

/**
 * Enum for user roles, shared for authorization logic.
 */
enum class Role {
    USER,
    ADMIN;

    fun canAccessAdmin(): Boolean = this == ADMIN
}

/**
 * Abstract base class for domain-specific errors related to User.
 * - Changed from sealed to abstract to allow extension across modules.
 * - Subclasses can be sealed in their own modules if needed.
 */
abstract class UserError(override val message: String? = null) : Exception(message) {
    // Common method for user-facing messages (override in subclasses)
    open fun getUserMessage(): String = message ?: "An error occurred."
}

// Example shared subclasses (keep sealed here if not extended elsewhere)
sealed class SharedUserError : UserError() {
    data object NotFound : SharedUserError() {
        private fun readResolve(): Any = NotFound
        override fun getUserMessage(): String = "User not found."
    }

    data class InvalidEmail(override val message: String) : SharedUserError()
    data object AlreadyExists : SharedUserError() {
        private fun readResolve(): Any = AlreadyExists
        override fun getUserMessage(): String = "User already exists."
    }
}