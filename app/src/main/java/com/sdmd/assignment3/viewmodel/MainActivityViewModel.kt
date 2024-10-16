package com.sdmd.assignment3.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdmd.assignment3.database.Firestore
import com.sdmd.assignment3.model.Profile
import com.sdmd.assignment3.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val MainActivityViewModelTAG = "MainActivityViewModelTAG"

class MainActivityViewModel(private val repository: Repository = Firestore) : ViewModel() {
    private val _selectedCategory = MutableLiveData("All")
    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    private var _profiles = MutableLiveData<List<Profile>>(listOf())
    val profiles: LiveData<List<Profile>>
        get() = _profiles

    fun selectCategory(category: String) {
        _selectedCategory.postValue(category)
    }

    init {
        getAllProfiles()
    }

    // Get all profiles from database (either from local Room or remote Firestore)
    private fun getAllProfiles() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Fetch the profiles from the repository
                val profileList = repository.getAllProfiles()
                _profiles.postValue(profileList)
                Log.d(MainActivityViewModelTAG, "Current List $profileList")
            } catch (e: Exception) {
                Log.e(MainActivityViewModelTAG, "Error fetching profiles: ${e.localizedMessage}")
            }
        }
    }

    // Insert new profile to Firestore
    fun insertProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProfile(profile)
            getAllProfiles()
        }
    }

    // Delete a profile from Firestore
    fun deleteProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProfile(profile)
            getAllProfiles()
        }
    }

    // Update a profile in Firestore
    fun updateProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateProfile(profile)
            getAllProfiles()
        }
    }
}