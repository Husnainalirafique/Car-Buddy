package com.example.carbuddy.ui.fragments.vendorProfile.data

import androidx.annotation.Keep
import com.google.firebase.firestore.GeoPoint

@Keep
data class ModelVendorDetail(
    val contactNumber: String,
    val experience: String,
    val city: String,
    val address: String,
    val location: GeoPoint,
    val shopName: String,
    val availability: String,
    val likes: Long,
) {
    constructor() : this("", "", "", "", GeoPoint(0.0, 0.0), "", "", 0L)
}
