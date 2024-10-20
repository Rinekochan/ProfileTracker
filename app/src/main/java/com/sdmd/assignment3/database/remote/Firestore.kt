package com.sdmd.assignment3.database.remote

import android.util.Log
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sdmd.assignment3.model.Profile
import kotlinx.coroutines.tasks.await

const val FirestoreTAG = "Firestore"

// Remote database for this app
object Firestore {
    // Get all profiles from firestore
    suspend fun getAllProfiles(): List<Profile> {
        val db = Firebase.firestore
        val profiles = mutableListOf<Profile>()

        // Get all documents from profiles collection
        try {
            val snapshot = db.collection("profiles").get().await() // Await for Firestore to finish fetching
            // Convert Firestore documents to a list of Profile objects
            for (item in snapshot.documents) {
                val product = Profile(
                    id = item.id,
                    name = item.data!!["name"] as String?,
                    birthday = item.data!!["birthday"] as String?,
                    gender = item.data!!["gender"] as String?,
                    phone = item.data!!["phone"] as String?,
                    address = item.data!!["address"] as String?,
                    suburb = item.data!!["suburb"] as String?,
                    category = item.data!!["category"] as String?,
                )
                profiles.add(product)
            }
            Log.d(FirestoreTAG, "Remote Read Success")
            return profiles
        } catch (e: Exception) {
            Log.e(FirestoreTAG, "Remote Read Failure: ${e.localizedMessage}")
            return mutableListOf()
        }
    }

    // Insert new profile to firestore
    suspend fun insertProfile(profile: Profile) {
        val db = Firebase.firestore

         try {
            val ref = db.collection("profiles")
                .add(convertToDocument(profile)).await() // Await Firestore insertion

            // Get the auto-generated ID and assign it to the profile
            profile.id = ref.id
            Log.d(FirestoreTAG, "Remote Insert Profile Success")
        } catch (e: Exception) {
            Log.e(FirestoreTAG, "Remote Insert Profile Failure: ${e.localizedMessage}")
        }
    }

    // Delete profile from firestore
    suspend fun deleteProfile(profile: Profile) {
        val db = Firebase.firestore

        // Delete document from profiles collection
        try {
            db.collection("profiles")
                .document(profile.id).delete().await() // Await Firestore insertion
            Log.d(FirestoreTAG, "Remote Delete Profile Success")
        } catch (e: Exception) {
            Log.e(FirestoreTAG, "Remote Delete Profile Failure: ${e.localizedMessage}")
        }
    }

    // Update profile from firestore
    suspend fun updateProfile(profile: Profile) {
        val db = Firebase.firestore

        // Update document in profiles collection
        try {
            db.collection("profiles")
                .document(profile.id).update(convertToDocument(profile)).await() // Await Firestore insertion
            Log.d(FirestoreTAG, "Remote Update Profile Success")
        } catch (e: Exception) {
            Log.e(FirestoreTAG, "Remote Update Profile Failure: ${e.localizedMessage}")
        }
    }

    // Convert Profile object into Json document for firestore
    private fun convertToDocument(profile: Profile): HashMap<String, Any?> {
        val profileDocument = HashMap<String, Any?>()
        profileDocument.apply {
            put("id", profile.id)
            put("name", profile.name)
            put("birthday", profile.birthday)
            put("gender", profile.gender)
            put("phone", profile.phone)
            put("address", profile.address)
            put("suburb", profile.suburb)
            put("category", profile.category)
        }
        return profileDocument
    }
}