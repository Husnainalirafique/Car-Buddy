package com.example.carbuddy.data.models.vehicles

data class ModelVehicle(
    val model: String,
    val make: String,
    val year: Int,
    val lpn: String,
    var docId: String = ""
) {
    constructor() : this("", "", 0, "")
}
