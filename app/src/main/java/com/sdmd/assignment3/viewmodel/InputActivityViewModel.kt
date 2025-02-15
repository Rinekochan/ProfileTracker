package com.sdmd.assignment3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sdmd.assignment3.model.Profile

class InputActivityViewModel : ViewModel() {
    private val _currentProfile = MutableLiveData(Profile("None", null, null, null, null, null, null, null))
    val currentProfile: LiveData<Profile>
        get() = _currentProfile

    private var _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int>
        get() = _currentPage

    // Set the whole profile information, used when modifying profile
    fun setProfile(profile: Profile) {
        _currentProfile.value?.let{
            it.id = profile.id
        }

        setPersonalDetails(profile.name!!, profile.birthday, profile.gender, profile.category!!)
        setContactDetails(profile.phone!!, profile.address, profile.suburb)
    }

    // Set personal profile information
    fun setPersonalDetails(name: String, birthday: String?, gender: String?, category: String) {
        _currentProfile.value?.let {
            it.name = name
            it.birthday = birthday
            it.gender = gender
            it.category = category
        }
    }

    // Set contact profile information
    fun setContactDetails(phone: String, address: String?, suburb: String?) {
        _currentProfile.value?.let {
            it.phone = phone
            it.address = address
            it.suburb = suburb
        }
    }

    // Set from PersonalFragment to ContactFragment
    fun setNextPage() {
        _currentPage.postValue(_currentPage.value!! + 1)
    }

    // Set from ContactFragment back to PersonalFragment
    fun setPreviousPage() {
        _currentPage.postValue(_currentPage.value!! - 1)
    }
}