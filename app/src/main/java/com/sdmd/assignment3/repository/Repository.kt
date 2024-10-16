package com.sdmd.assignment3.repository

import com.sdmd.assignment3.model.Profile

interface Repository {
    suspend fun getAllProfiles(): List<Profile>
    suspend fun insertProfile(profile: Profile)
    suspend fun deleteProfile(profile: Profile)
    suspend fun updateProfile(profile: Profile)
}