package com.example.carbuddy.ui.fragments.bookingFlow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carbuddy.data.models.booking.ModelBookingData
import com.example.carbuddy.data.models.vendor.ModelVendorProfile
import com.example.carbuddy.data.remote.FcmApi
import com.example.carbuddy.data.remote.models.Message
import com.example.carbuddy.data.remote.models.NotificationData
import com.example.carbuddy.data.remote.models.NotificationMessage
import com.example.carbuddy.repositories.ProfileRepository
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.FcmAccessTokenManager
import com.example.carbuddy.utils.withIoContext
import com.example.carbuddy.utils.withMainContext
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val firebaseApiService: FcmApi,
    private val accessTokenProvider: FcmAccessTokenManager,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _bookingStatus = MutableLiveData<DataState<Nothing>>()
    val bookingStatus: LiveData<DataState<Nothing>> = _bookingStatus

    val fetchVehicles = profileRepository.fetchVehicles

    init {
        fetchVehicles()
    }

    suspend fun saveBookingToDb(
        bookingData: ModelBookingData,
        vendor: ModelVendorProfile
    ) {
        try {
            withMainContext { _bookingStatus.value = DataState.Loading }
            db.collection("bookings")
                .add(bookingData)
                .await()

            // If successful, send the notification on a background thread
            withIoContext { sendNotification(vendor) }
        } catch (e: Exception) {
            withMainContext {
                _bookingStatus.value = DataState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    private suspend fun sendNotification(vendor: ModelVendorProfile) {
        val accessToken = accessTokenProvider.getAccessToken()
        val data = NotificationData("You got a new order 😍", "Hi ${vendor.fullName}")
        accessToken?.let { serverKey ->
            val message = Message(NotificationMessage(vendor.fcmToken, data))

            try {
                val response = firebaseApiService.sendNotification(
                    authorization = "Bearer $serverKey",
                    notification = message
                )

                if (response.isSuccessful) {
                    withMainContext { _bookingStatus.value = DataState.Success() }
                } else {
                    withMainContext {
                        _bookingStatus.value = DataState.Error("Error Sending Notification")
                    }
                }
            } catch (e: Exception) {
                withMainContext { _bookingStatus.value = DataState.Error(e.localizedMessage!!) }
            }
        }
    }

    private fun fetchVehicles() {
        viewModelScope.launch {
            profileRepository.fetchVehicles()
        }
    }
}