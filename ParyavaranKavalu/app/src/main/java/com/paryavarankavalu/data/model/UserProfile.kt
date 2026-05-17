package com.paryavarankavalu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Eco Warrior",
    val role: UserRole = UserRole.USER,
    val totalReports: Int = 0,
    val cleanedReports: Int = 0,
    val ecoKarmaPoints: Int = 0,
    val level: EcoLevel = EcoLevel.SEEDLING,
    val joinDate: Long = System.currentTimeMillis()
)

enum class UserRole {
    USER,
    ADMIN
}

enum class EcoLevel(val title: String, val minPoints: Int, val emoji: String) {
    SEEDLING("Seedling", 0, "🌱"),
    SPROUT("Sprout", 50, "🌿"),
    SAPLING("Sapling", 150, "🌳"),
    GUARDIAN("Eco Guardian", 350, "🌲"),
    CHAMPION("Eco Champion", 700, "🏆"),
    LEGEND("Green Legend", 1500, "⭐")
}
