package com.sdmd.assignment3.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel : ViewModel() {
    private val _selectedCategory = MutableLiveData("All")
    val selectedCategory: LiveData<String>
        get() = _selectedCategory

    fun selectCategory(category: String) {
        _selectedCategory.postValue(category)
    }
}