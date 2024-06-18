package com.example.carbuddy.data.remote.models

data class Message(
    val message: NotificationMessage
)

data class NotificationMessage(
    val token: String,
    val notification: NotificationData
)

data class NotificationData(
    val body: String,
    val title: String
)