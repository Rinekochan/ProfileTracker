package com.sdmd.assignment3.database

import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.sdmd.assignment3.model.Profile
import com.sdmd.assignment3.repository.Repository
import kotlinx.coroutines.tasks.await

const val FirestoreTAG = "Firestore"
// Remote database for this app. Source of truth
object Firestore : Repository {
    // Get all profiles from firestore
    override suspend fun getAllProfiles(): List<Profile> {
        val db = Firebase.firestore
        val profiles = mutableListOf<Profile>()

        // Get all documents from profiles collection
        return try {
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
                    createDate = item.data!!["createDate"] as Timestamp?,
                    modifyDate = item.data!!["modifyDate"] as Timestamp?,
                )
                profiles.add(product)
            }

            return profiles
        } catch (e: Exception) {
            Log.e(FirestoreTAG, "Read Failure: ${e.localizedMessage}")
            return mutableListOf()
        }
    }

    // Insert new profile to firestore
    override fun insertProfile(profile: Profile) {
        val db = Firebase.firestore

        // Add document to profiles collection
        db.collection("profiles")
            .add(convertToDocument(profile))
            .addOnSuccessListener { ref ->
                profile.id = ref.id  // Get the auto-generated ID
                Log.d(FirestoreTAG, "Insert Profile Success")
            }
            .addOnFailureListener {
                Log.e(FirestoreTAG, "Insert Profile Failure ${it.localizedMessage!!}")
            }
    }

    // Delete profile from firestore
    override fun deleteProfile(profile: Profile) {
        val db = Firebase.firestore

        // Delete document from profiles collection
        db.collection("profiles").document(profile.id!!)
            .delete()
            .addOnSuccessListener {
                Log.d(FirestoreTAG, "Delete Profile Success")
            }
            .addOnFailureListener {
                Log.e(FirestoreTAG, "Delete Failure ${it.localizedMessage!!}")
            }
    }

    // Update profile from firestore
    override fun updateProfile(profile: Profile) {
        val db = Firebase.firestore

        // Update document in profiles collection
        db.collection("profiles").document(profile.id!!)
            .update(convertToDocument(profile))
            .addOnSuccessListener {
                Log.d("Database", "Update Profile Success")
            }
            .addOnFailureListener {
                Log.e("Database", "Update Failure ${it.localizedMessage!!}")
            }
    }

    // Convert Profile object into Json document for firestore
    private fun convertToDocument(profile: Profile) : HashMap<String, Any?> {
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
            put("createDate", profile.createDate)
            put("modifyDate", profile.modifyDate)
        }
        return profileDocument
    }
}