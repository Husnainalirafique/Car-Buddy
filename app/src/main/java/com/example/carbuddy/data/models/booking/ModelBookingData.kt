package com.example.carbuddy.data.models.booking


import androidx.annotation.Keep

@Keep
data class ModelBookingData(
    val address: String,
    val detail: String,
    val latLng: String,
    val userFcmToken: String,
    val userUid: String,
    val vendorUid: String,
    var bookingStatus: String,
    val userImageUri: String,
    var bookingTypeTag: String,
    val userName: String,
    val vehicleModel: String,
    val vehicleMake: String,
    val vehicleYear: String,
    val vehicleLpn: String,
    var accepted: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "", "", "", "", "", "", "", "", "", "", false)
}
