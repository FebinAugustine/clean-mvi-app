package com.febin.feature.authentication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.febin.shared_domain.model.User  // Import for fromDomain param

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val email: String,
    val name: String?,
    val phone: String?,
    val role: String = "USER",
    val isVerified: Boolean = false
) {
    companion object {
        fun fromDomain(user: User): UserEntity = UserEntity(
            id = user.id,
            email = user.email,
            name = user.name,
            phone = user.phone,
            role = user.role.name,
            isVerified = user.isVerified
        )
    }
}