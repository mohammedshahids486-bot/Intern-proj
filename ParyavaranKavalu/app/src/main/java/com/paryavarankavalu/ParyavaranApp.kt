package com.paryavarankavalu

import android.app.Application
import com.paryavarankavalu.data.local.AppDatabase
import com.paryavarankavalu.data.repository.ReportRepository

class ParyavaranApp : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }

    val repository by lazy {
        ReportRepository(
            database.wasteReportDao(),
            database.userProfileDao()
        )
    }
}
