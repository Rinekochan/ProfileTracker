package com.sdmd.assignment3.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import com.google.firebase.Timestamp

// This class stores the profile details
@Parcelize
data class Profile(
    var id: Int?,
    var name: String?,
    var birthday: String?,
    var gender: String?,
    var phone: String?,
    var address: String?,
    var suburb: String?,
    var category: String?,
    var createDate: Timestamp? = null,
    var modifyDate: Timestamp? = null
    ) : Parcelable
