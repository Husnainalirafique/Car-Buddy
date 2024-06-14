package com.example.carbuddy.ui.fragments.bookingFlow

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.carbuddy.data.Message
import com.example.carbuddy.data.NotificationData
import com.example.carbuddy.data.NotificationMessage
import com.example.carbuddy.data.remote.FcmApi
import com.example.carbuddy.utils.FcmAccessTokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val firebaseApiService: FcmApi,
    private val accessTokenProvider: FcmAccessTokenManager,
) : ViewModel() {

    suspend fun sendNotification(fcmToken: String, data: NotificationData) {
        val accessToken = accessTokenProvider.getAccessToken()

        accessToken?.let { serverKey ->
            val message = Message(NotificationMessage(fcmToken, data))

            try {
                val response = firebaseApiService.sendNotification(
                    authorization = "Bearer $serverKey",
                    notification = message
                )

                if (response.isSuccessful) {
                    Log.d(
                        "Notification",
                        "Notification sent successfully: ${response.body()?.string()}"
                    )
                } else {
                    Log.e(
                        "Notification",
                        "Error sending notification: ${response.errorBody()?.string()}"
                    )
                }
            } catch (e: Exception) {
                Log.e("Notification", "Exception sending notification: $e")
            }
        }
    }
}