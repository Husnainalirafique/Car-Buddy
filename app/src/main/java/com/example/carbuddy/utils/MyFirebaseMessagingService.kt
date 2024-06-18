package com.example.carbuddy.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.example.carbuddy.R
import com.example.carbuddy.ui.activities.AfterAuthActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val channelId = "Husnain"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val intent = Intent(this, AfterAuthActivity::class.java)
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE)
        createNotificationChannel(manager as NotificationManager)

        val intent1 =
            PendingIntent.getActivities(this, 0, arrayOf(intent), PendingIntent.FLAG_IMMUTABLE)

        // Handle both notification and data payloads
        val notificationTitle = message.notification?.title ?: message.data["title"]
        val notificationBody = message.notification?.body ?: message.data["body"]
        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle(notificationTitle)
            .setContentText(notificationBody)
            .setSmallIcon(R.drawable.icon_location_marker)
            .setAutoCancel(true)
            .setContentIntent(intent1)
            .build()

        manager.notify(Random.nextInt(), notification)
    }

    private fun createNotificationChannel(manager: NotificationManager) {
        val channel =
            NotificationChannel(channelId, "Car Buddy", NotificationManager.IMPORTANCE_HIGH)
        channel.description = "new channel"
        channel.enableLights(true)
        manager.createNotificationChannel(channel)
    }
}