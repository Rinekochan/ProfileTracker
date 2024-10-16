package com.sdmd.assignment3.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.sdmd.assignment3.database.local.ProfileDao
import com.sdmd.assignment3.database.local.RoomTAG
import com.sdmd.assignment3.database.remote.Firestore
import com.sdmd.assignment3.database.remote.FirestoreTAG
import com.sdmd.assignment3.model.Profile

const val ProfileRepositoryTAG = "ProfileRepository"
// This repository helps syncing between local and remote database, and fetching data from local database
class ProfileRepository(
    private val profileDao: ProfileDao, // Room DAO for local database
    private val firestore: Firestore // Firestore object for remote database
) : Repository {

    private var isSyncPerformed = false // The sync process only happens once when the class is created

    // Fetch all profiles - get from Room, if empty then get from Firestore
    @WorkerThread
    override suspend fun getAllProfiles(): List<Profile> {
        // Fetch profiles from Room first
        var profiles = profileDao.getAllProfiles()

        // If Room is empty, sync with Firestore
        if (profiles.isEmpty()) {
            Log.d(ProfileRepositoryTAG, "Local database is empty. Populating with remote data")
            profiles = firestore.getAllProfiles()
            // Cache Firestore data into Room
            profiles.forEach {
                profileDao.insertProfile(it)
            }
        }
        Log.d(RoomTAG, "Local Read Success")
        return profiles
    }

    @WorkerThread
    override suspend fun insertProfile(profile: Profile) {
        firestore.insertProfile(profile)  // Save remotely in Firestore
        profileDao.insertProfile(profile) // Save locally in Room
        Log.d(RoomTAG, "Local Insert Success")
    }

    @WorkerThread
    override suspend fun deleteProfile(profile: Profile) {
        firestore.deleteProfile(profile)  // Delete remotely in Firestore
        profileDao.deleteProfile(profile) // Delete locally in Room
        Log.d(RoomTAG, "Local Delete Success")
    }

    @WorkerThread
    override suspend fun updateProfile(profile: Profile) {
        firestore.updateProfile(profile)  // Update remotely in Firestore
        profileDao.updateProfile(profile) // Update locally in Room
        Log.d(RoomTAG, "Local Update Success")
    }
}