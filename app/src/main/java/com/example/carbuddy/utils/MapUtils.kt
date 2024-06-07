package com.example.carbuddy.utils

object MapUtils {
    fun extractLatLong(text: String): Pair<Double, Double>? {
        val (latStr, longStr) = text.split(",") // Split the text by comma

        return try {
            // Extract latitude and longitude parts and convert them to Double
            val lat = latStr.toDouble()
            val long = longStr.toDouble()

            // Return latitude and longitude as Pair<Double, Double>
            Pair(lat, long)
        } catch (e: NumberFormatException) {
            // If conversion to Double fails, return null
            null
        }
    }
}