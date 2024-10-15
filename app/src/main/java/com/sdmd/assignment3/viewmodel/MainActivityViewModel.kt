package com.sdmd.assignment3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sdmd.assignment3.model.Profile
import com.sdmd.assignment3.repository.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(private val repository: Repository) : ViewModel() {
    private val _selectedCategory = MutableLiveData("All")
    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    private var _profiles = MutableLiveData<List<Profile>>(listOf())
    val profiles: LiveData<List<Profile>>
        get() = _profiles

    fun selectCategory(category: String) {
        _selectedCategory.postValue(category)
    }

    // Get all profiles from database (either from local Room or remote Firestore)
    fun getAllProfiles() {
        viewModelScope.launch(Dispatchers.IO){
            _profiles.postValue(repository.getAllProfiles())
        }
    }

    // Insert new profile to Firestore
    fun insertProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO){
            repository.insertProfile(profile)
            getAllProfiles()
        }
    }

    // Delete a profile from Firestore
    fun deleteProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteProfile(profile)
            getAllProfiles()
        }
    }

    // Update a profile in Firestore
    fun updateProfile(profile: Profile) {
        viewModelScope.launch(Dispatchers.IO){
            repository.updateProfile(profile)
            getAllProfiles()
        }
    }
}