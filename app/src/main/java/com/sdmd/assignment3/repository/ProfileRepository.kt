package com.sdmd.assignment3.repository

import android.util.Log
import androidx.annotation.WorkerThread
import com.sdmd.assignment3.database.local.ProfileDao
import com.sdmd.assignment3.database.local.RoomTAG
import com.sdmd.assignment3.database.remote.Firestore
import com.sdmd.assignment3.database.remote.FirestoreTAG
import com.sdmd.assignment3.model.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        } else {
            // Launch a coroutine to synchronize databases without blocking, the app can display the updated list later
            CoroutineScope(Dispatchers.Default).launch {
                synchronizeDatabases()
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

    // Note: This sync process works because our local Room database is the source of truth
    //       The remote database is to store it remotely so it won't be deleted if the app is uninstalled
    @WorkerThread
    private suspend fun synchronizeDatabases() {
        Log.i(ProfileRepositoryTAG, "Synchronise local and remote databases")
        if (isSyncPerformed) return  // Only perform sync once

        val roomProfiles = profileDao.getAllProfiles()
        val remoteProfiles = firestore.getAllProfiles()
        Log.d(RoomTAG, "Current Local Database $roomProfiles")
        Log.d(FirestoreTAG, "Current Remote Database $remoteProfiles")

        val remoteProfilesIds = remoteProfiles.map { it.id }.toSet()

        // Insert missing profiles from Room to Firestore
        for (roomProfile in roomProfiles) {
            if (!remoteProfilesIds.contains(roomProfile.id)) {
                Log.d(ProfileRepositoryTAG, "Profile ${roomProfile.id} is missing remotely.")
                firestore.insertProfile(roomProfile)
            }
        }

        // Check for differences between Room and Firestore
        for (roomProfile in roomProfiles) {
            val firestoreProfile = remoteProfiles.find{ it.id == roomProfile.id }
            if (roomProfile != firestoreProfile) {
                Log.d(ProfileRepositoryTAG, "Profile ${roomProfile.id} is different remotely.")
                firestore.updateProfile(roomProfile)
            }
        }

        isSyncPerformed = true // Mark sync as done
    }
}