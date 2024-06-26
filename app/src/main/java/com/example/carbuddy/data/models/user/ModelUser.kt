package com.example.carbuddy.data.models.user

data class ModelUser(
    val email: String,
    val password: String,
    val fullName: String,
    val dob: String,
    val phoneNumber: String,
    val address: String,
    var profileImageUri: String,
    val fcmToken: String,
    var docId: String = ""
) {
    constructor() : this("", "", "", "", "", "", "", "")
}