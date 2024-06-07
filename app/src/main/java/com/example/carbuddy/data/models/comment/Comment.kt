package com.example.carbuddy.data.models.comment

import androidx.annotation.Keep

@Keep
data class Comment(
    val userName: String,
    val comment: String,
    var docId: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    constructor() : this("", "")
}