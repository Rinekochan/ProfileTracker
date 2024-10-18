package com.sdmd.assignment3.database.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sdmd.assignment3.model.Profile

const val RoomTAG = "Room"
// Room database for local cache from Firestore
@Database(entities = [Profile::class],version=1,exportSchema = false)
abstract class ProfileDatabase : RoomDatabase() {
    abstract fun profileDatabaseDao(): ProfileDao

    companion object {
        // Singleton prevents multiple instances of database opening
        @Volatile
        private var INSTANCE: ProfileDatabase? = null

        fun getRoomDatabase(
            context: Context,
        ): ProfileDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProfileDatabase::class.java,
                        "profiles_database"
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

