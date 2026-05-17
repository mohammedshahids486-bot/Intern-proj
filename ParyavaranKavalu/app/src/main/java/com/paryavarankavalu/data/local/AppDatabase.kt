package com.paryavarankavalu.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.paryavarankavalu.data.model.UserProfile
import com.paryavarankavalu.data.model.WasteReport
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [WasteReport::class, UserProfile::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun wasteReportDao(): WasteReportDao
    abstract fun userProfileDao(): UserProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "paryavaran_kavalu_db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Pre-populate with default user profile
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.userProfileDao().insertProfile(UserProfile())
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
