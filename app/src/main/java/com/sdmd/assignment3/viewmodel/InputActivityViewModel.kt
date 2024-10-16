package com.sdmd.assignment3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Timestamp
import com.sdmd.assignment3.model.Profile

class InputActivityViewModel : ViewModel() {
    private val _currentProfile = MutableLiveData(Profile(null, null, null, null, null, null, null, null, null, null))
    val currentProfile: LiveData<Profile>
        get() = _currentProfile

    private var _currentPage = MutableLiveData(0)
    val currentPage: LiveData<Int>
        get() = _currentPage

    fun setProfile(profile: Profile) {
        setPersonalDetails(profile.name!!, profile.birthday, profile.gender, profile.category!!)
        setContactDetails(profile.phone!!, profile.address, profile.suburb)
    }

    fun setPersonalDetails(name: String, birthday: String?, gender: String?, category: String) {
        _currentProfile.value?.let {
            it.name = name
            it.birthday = birthday
            it.gender = gender
            it.category = category
        }
    }

    fun setContactDetails(phone: String, address: String?, suburb: String?) {
        _currentProfile.value?.let {
            it.phone = phone
            it.address = address
            it.suburb = suburb
            it.createDate = Timestamp.now()
        }
    }

    fun setNextPage() {
        _currentPage.postValue(_currentPage.value!! + 1)
    }

    fun setPreviousPage() {
        _currentPage.postValue(_currentPage.value!! - 1)
    }
}