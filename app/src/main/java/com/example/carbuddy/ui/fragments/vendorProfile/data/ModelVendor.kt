package com.example.carbuddy.ui.fragments.vendorProfile.data

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ModelVendor(
    val vendorProfileImage: String,
    val vendorName: String,
    val vendorSpeciality: String,
    var vendorUid: String
) : Parcelable {
    constructor() : this("", "", "", "")
}
