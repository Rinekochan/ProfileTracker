package com.sdmd.assignment3.repository

import com.sdmd.assignment3.model.Profile

interface Repository {
    suspend fun getAllProfiles(): List<Profile>
    fun insertProfile(profile: Profile)
    fun deleteProfile(profile: Profile)
    fun updateProfile(profile: Profile)
}