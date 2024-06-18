package com.example.carbuddy.data.models.vendor

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ModelVendorProfile(
    val vendorImage: String = "",
    val fullName: String = "",
    val contactNumber: String = "",
    val whatsappNumber: String = "",
    val speciality: String = "",
    val yearsOfExperience: String = "",
    val city: String = "",
    val shopName: String = "",
    val availability: String = "",
    val locationNameInCity: String = "",
    val addressFromMap: String = "",
    val vendorUid: String = "",
    val fcmToken: String = ""
) : Parcelable