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
 * Sealed class for domain-specific errors related to User.
 */
sealed class UserError : Exception() {
    data object NotFound : UserError() {
        private fun readResolve(): Any = NotFound
    }

    data class InvalidEmail(override val message: String) : UserError()
    data object AlreadyExists : UserError() {
        private fun readResolve(): Any = AlreadyExists
    }
}