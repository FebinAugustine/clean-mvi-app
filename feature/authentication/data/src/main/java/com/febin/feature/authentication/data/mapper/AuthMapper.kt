package com.febin.feature.authentication.data.mapper

import com.febin.feature.authentication.data.dto.network.UserDto
import com.febin.feature.authentication.data.dto.network.TokenDto
import com.febin.feature.authentication.domain.model.AuthToken
import com.febin.feature.authentication.domain.model.LoginError
import com.febin.feature.authentication.domain.model.SignupError
import com.febin.feature.authentication.domain.model.TokenType
import com.febin.shared_domain.model.Role
import com.febin.shared_domain.model.SharedUserError  // Added: Import for SharedUserError
import com.febin.shared_domain.model.User


/**
 * Mappers for Auth DTOs ↔ Domain models.
 * - Bidirectional; handles enum/string conversions.
 * - Error mapping from exceptions/HTTP codes (used in repo).
 */
object AuthMapper {

    // DTO to Domain
    fun toDomain(userDto: UserDto): User = User(
        id = userDto.id,
        email = userDto.email,
        name = userDto.name,
        phone = userDto.phone,
        role = when (userDto.role.uppercase()) {
            "ADMIN" -> Role.ADMIN
            else -> Role.USER
        },
        isVerified = false  // Fixed: Default to false (add 'val verified: Boolean? = null' to UserDto if needed)
    )

    fun toDomain(tokenDto: TokenDto): AuthToken = AuthToken(
        token = tokenDto.token,
        expiresIn = tokenDto.expiresIn,
        refreshToken = tokenDto.refreshToken,
        type = when (tokenDto.type.uppercase()) {
            "JWT" -> TokenType.JWT
            else -> TokenType.BEARER
        }
    )

    // Domain to DTO (for requests)
    fun fromDomain(user: User): UserDto = UserDto(
        id = user.id,
        email = user.email,
        name = user.name,
        phone = user.phone,
        role = user.role.name  // Enum to string
    )

    fun fromDomain(token: AuthToken): TokenDto = TokenDto(
        token = token.token,
        expiresIn = token.expiresIn,
        refreshToken = token.refreshToken,
        type = token.type.name  // Enum to string
    )

    // Error mapping (from exceptions/HTTP to domain)
    fun mapToLoginError(throwable: Throwable, httpCode: Int? = null): LoginError = when {
        httpCode == 401 -> LoginError.InvalidCredentials
        httpCode == 403 -> LoginError.AccountNotVerified
        httpCode == 0 || throwable.message?.contains("network") == true -> LoginError.NetworkUnavailable
        throwable is SharedUserError.InvalidEmail -> LoginError.Unknown(throwable.message)  // Fixed: SharedUserError.InvalidEmail
        else -> LoginError.Unknown(throwable.message ?: "Login failed")
    }

    fun mapToSignupError(throwable: Throwable, httpCode: Int? = null): SignupError = when {
        httpCode == 409 -> SignupError.EmailAlreadyExists
        httpCode == 400 -> SignupError.InvalidPhone
        throwable is SharedUserError.AlreadyExists -> SignupError.EmailAlreadyExists  // Fixed: SharedUserError.AlreadyExists
        throwable.message?.contains("required") == true -> SignupError.MissingRequiredField("Missing field")
        else -> SignupError.MissingRequiredField(throwable.message ?: "Signup failed")
    }
}