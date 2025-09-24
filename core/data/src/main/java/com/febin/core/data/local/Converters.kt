package com.febin.core.data.local

import androidx.room.TypeConverter
import com.febin.shared_domain.model.Role  // Example for Role enum

/**
 * Room TypeConverters for enums/strings.
 * - Extend for other types (e.g., TokenType).
 */
class Converters {
    @TypeConverter
    fun fromRole(role: Role): String = role.name

    @TypeConverter
    fun toRole(roleString: String): Role = enumValueOf(roleString)
}