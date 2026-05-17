package com.paryavarankavalu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.paryavarankavalu.data.model.ReportStatus
import com.paryavarankavalu.data.model.WasteReport

@Dao
interface WasteReportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: WasteReport): Long

    @Update
    suspend fun updateReport(report: WasteReport)

    @Delete
    suspend fun deleteReport(report: WasteReport)

    @Query("SELECT * FROM waste_reports ORDER BY timestamp DESC")
    fun getAllReports(): LiveData<List<WasteReport>>

    @Query("SELECT * FROM waste_reports WHERE status = :status ORDER BY timestamp DESC")
    fun getReportsByStatus(status: ReportStatus): LiveData<List<WasteReport>>

    @Query("SELECT * FROM waste_reports WHERE id = :id")
    suspend fun getReportById(id: Long): WasteReport?

    @Query("UPDATE waste_reports SET status = :status WHERE id = :id")
    suspend fun updateStatus(id: Long, status: ReportStatus)

    @Query("SELECT COUNT(*) FROM waste_reports")
    suspend fun getTotalReports(): Int

    @Query("SELECT COUNT(*) FROM waste_reports WHERE status = 'CLEANED'")
    suspend fun getCleanedReports(): Int

    @Query("SELECT SUM(ecoKarmaPoints) FROM waste_reports WHERE status = 'CLEANED' OR status = 'VERIFIED'")
    suspend fun getTotalKarmaPoints(): Int?
}
