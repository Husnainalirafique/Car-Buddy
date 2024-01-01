package com.example.carbuddy.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {
    fun formatHoursMinutes(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    fun formatCompleteDateAndTime():String{
        return SimpleDateFormat("yyyy_mm_dd_hh_mm_ss", Locale.getDefault()).format(Date())
    }
    fun formatMDY(date: Date): String {
        val format = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        return format.format(date)
    }
}