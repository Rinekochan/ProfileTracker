package com.sdmd.assignment3.database.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.sdmd.assignment3.model.Profile

@Dao
interface ProfileDao {
    @Query("Select * FROM profiles ORDER BY name ASC")
    suspend fun getAllProfiles(): List<Profile>

    @Insert
    suspend fun insertProfile(profile: Profile)

    @Delete
    suspend fun deleteProfile(profile: Profile)

    @Update
    suspend fun updateProfile(profile: Profile)
}