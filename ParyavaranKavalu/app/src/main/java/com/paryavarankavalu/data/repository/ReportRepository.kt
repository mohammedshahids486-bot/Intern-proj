package com.paryavarankavalu.data.repository

import androidx.lifecycle.LiveData
import com.paryavarankavalu.data.local.UserProfileDao
import com.paryavarankavalu.data.local.WasteReportDao
import com.paryavarankavalu.data.model.EcoLevel
import com.paryavarankavalu.data.model.ReportStatus
import com.paryavarankavalu.data.model.UserProfile
import com.paryavarankavalu.data.model.WasteReport

class ReportRepository(
    private val reportDao: WasteReportDao,
    private val profileDao: UserProfileDao
) {
    // ── Reports ──────────────────────────────────────────────
    val allReports: LiveData<List<WasteReport>> = reportDao.getAllReports()

    fun getReportsByStatus(status: ReportStatus) = reportDao.getReportsByStatus(status)

    suspend fun insertReport(report: WasteReport): Long {
        val id = reportDao.insertReport(report)
        ensureProfileExists()
        profileDao.addReport(report.ecoKarmaPoints)
        return id
    }

    suspend fun updateReportStatus(id: Long, newStatus: ReportStatus) {
        reportDao.updateStatus(id, newStatus)
        if (newStatus == ReportStatus.CLEANED) {
            profileDao.incrementCleaned()
        }
        recalculateLevel()
    }

    suspend fun getReportById(id: Long) = reportDao.getReportById(id)

    suspend fun deleteReport(report: WasteReport) {
        reportDao.deleteReport(report)
    }

    // ── User Profile ──────────────────────────────────────────
    fun getUserProfile(): LiveData<UserProfile?> = profileDao.getProfile()

    private suspend fun ensureProfileExists() {
        if (profileDao.getProfileOnce() == null) {
            profileDao.insertProfile(UserProfile())
        }
    }

    private suspend fun recalculateLevel() {
        val profile = profileDao.getProfileOnce() ?: return
        val points = profile.ecoKarmaPoints
        val newLevel = EcoLevel.values()
            .filter { it.minPoints <= points }
            .maxByOrNull { it.minPoints } ?: EcoLevel.SEEDLING
        if (newLevel != profile.level) {
            profileDao.updateProfile(profile.copy(level = newLevel))
        }
    }

    suspend fun getTotalReports() = reportDao.getTotalReports()
    suspend fun getCleanedReports() = reportDao.getCleanedReports()

    suspend fun updateUserRole(newRole: com.paryavarankavalu.data.model.UserRole) {
        val profile = profileDao.getProfileOnce() ?: UserProfile()
        profileDao.updateProfile(profile.copy(role = newRole))
    }
}
