package com.paryavarankavalu.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waste_reports")
data class WasteReport(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val wasteType: WasteType,
    val latitude: Double,
    val longitude: Double,
    val address: String = "",
    val photoPath: String = "",
    val status: ReportStatus = ReportStatus.PENDING,
    val timestamp: Long = System.currentTimeMillis(),
    val reporterName: String = "Anonymous",
    val ecoKarmaPoints: Int = 10
)

enum class WasteType(val displayName: String, val emoji: String) {
    PLASTIC("Plastic Waste", "🧴"),
    ORGANIC("Organic Waste", "🌿"),
    ELECTRONIC("E-Waste", "💻"),
    CONSTRUCTION("Construction Debris", "🏗️"),
    MEDICAL("Medical Waste", "🏥"),
    MIXED("Mixed Garbage", "🗑️"),
    LIQUID("Liquid Waste", "💧"),
    HAZARDOUS("Hazardous Material", "⚠️")
}

enum class ReportStatus(val displayName: String) {
    PENDING("Pending Cleanup"),
    VERIFIED("Verified"),
    IN_PROGRESS("In Progress"),
    CLEANED("Cleaned ✓")
}
