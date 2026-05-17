package com.paryavarankavalu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.paryavarankavalu.data.model.UserProfile

@Dao
interface UserProfileDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProfile(profile: UserProfile)

    @Update
    suspend fun updateProfile(profile: UserProfile)

    @Query("SELECT * FROM user_profile WHERE id = 1")
    fun getProfile(): LiveData<UserProfile?>

    @Query("SELECT * FROM user_profile WHERE id = 1")
    suspend fun getProfileOnce(): UserProfile?

    @Query("UPDATE user_profile SET ecoKarmaPoints = ecoKarmaPoints + :points, totalReports = totalReports + 1 WHERE id = 1")
    suspend fun addReport(points: Int)

    @Query("UPDATE user_profile SET cleanedReports = cleanedReports + 1 WHERE id = 1")
    suspend fun incrementCleaned()
}
