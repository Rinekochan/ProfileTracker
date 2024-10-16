package com.sdmd.assignment3.database

import android.app.Application
import com.sdmd.assignment3.database.local.ProfileDatabase.Companion.getRoomDatabase
import com.sdmd.assignment3.database.remote.Firestore
import com.sdmd.assignment3.repository.ProfileRepository

class ProfileApplication : Application() {
    // Using by lazy so the database and repository are only created when they're needed
    val database by lazy { getRoomDatabase(this) }
    val repository by lazy { ProfileRepository(database.profileDatabaseDao(), Firestore) }

}