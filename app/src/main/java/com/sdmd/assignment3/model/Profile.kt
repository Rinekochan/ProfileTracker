package com.sdmd.assignment3.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// This class stores the profile details
@Parcelize
@Entity(tableName = "profiles")
data class Profile(
    @PrimaryKey
    var id: String,

    @ColumnInfo(name="name")
    var name: String?,

    @ColumnInfo(name="birthday")
    var birthday: String?,

    @ColumnInfo(name="gender")
    var gender: String?,

    @ColumnInfo(name="phone")
    var phone: String?,

    @ColumnInfo(name="address")
    var address: String?,

    @ColumnInfo(name="suburb")
    var suburb: String?,

    @ColumnInfo(name="category")
    var category: String?,
    ) : Parcelable
