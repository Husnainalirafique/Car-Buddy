package com.example.carbuddy.ui.fragments.bookings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carbuddy.data.models.booking.ModelBookingData
import com.example.carbuddy.utils.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VmBooking @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val currentUserUid = auth.currentUser?.uid!!

    private val _bookingData = MutableLiveData<DataState<List<ModelBookingData>>>()
    val bookingsData: LiveData<DataState<List<ModelBookingData>>> = _bookingData

    private val _ongoingBookings = MutableLiveData<DataState<List<ModelBookingData>>>()
    val ongoingBookings: LiveData<DataState<List<ModelBookingData>>> = _ongoingBookings

    private val _completedBookings = MutableLiveData<DataState<List<ModelBookingData>>>()
    val completedBookings: LiveData<DataState<List<ModelBookingData>>> = _completedBookings

    fun getPendingBookings() {
        _bookingData.value = DataState.Loading
        db.collection("bookings")
            .whereEqualTo("userUid", currentUserUid)
            .whereEqualTo("accepted", false)
            .whereEqualTo("bookingStatus", "Requested")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _bookingData.value = DataState.Error(error.localizedMessage!!)
                    return@addSnapshotListener
                }
                if (value != null) {
                    val bookingList = value.documents.mapNotNull { document ->
                        document.toObject(ModelBookingData::class.java)
                    }
                    _bookingData.value = DataState.Success(bookingList)
                } else {
                    _bookingData.value = DataState.Error("No data found")
                }
            }
    }

    fun getOngoingBookings() {
        _ongoingBookings.value = DataState.Loading
        db.collection("bookings")
            .whereEqualTo("accepted", true)
            .whereEqualTo("bookingStatus", "In progress")
            .whereEqualTo("userUid", currentUserUid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _ongoingBookings.value = DataState.Error(error.localizedMessage!!)
                    return@addSnapshotListener
                }

                val bookingList = value?.documents?.mapNotNull { document ->
                    document.toObject(ModelBookingData::class.java)
                }
                _ongoingBookings.value = DataState.Success(bookingList ?: emptyList())
            }
    }

    fun getCompletedBookings() {
        _completedBookings.value = DataState.Loading
        db.collection("bookings")
            .whereIn("bookingStatus", listOf("Completed", "Canceled"))
            .whereEqualTo("userUid", currentUserUid)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    _completedBookings.value = DataState.Error(error.localizedMessage!!)
                    return@addSnapshotListener
                }

                val bookingList = value?.documents?.mapNotNull { document ->
                    document.toObject(ModelBookingData::class.java)
                }
                _completedBookings.value = DataState.Success(bookingList ?: emptyList())
            }
    }
}