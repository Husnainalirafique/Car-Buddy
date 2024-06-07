package com.example.carbuddy.ui.fragments.vendorProfile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carbuddy.utils.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VmVendor @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {
    private var _totalLikes = MutableLiveData<DataState<Long>>()
    var totalLikes: LiveData<DataState<Long>> = _totalLikes

    fun fetchTotalLikes(vendorUid: String) {
        _totalLikes.value = DataState.Loading
        db.collection("likes").document(vendorUid).get().addOnSuccessListener { document ->
            val totalLikes = document.getLong("totalLikes") ?: 0L
            _totalLikes.value = DataState.Success(totalLikes)
        }.addOnFailureListener { exception ->
            _totalLikes.value = DataState.Error(exception.localizedMessage!!)
        }
    }

    fun updateLike(isLiked: Boolean, vendorUid: String) {
        val userRef = db.collection("likes").document(vendorUid)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(userRef)
            val currentLikes = when {
                snapshot.exists() -> snapshot.getLong("totalLikes") ?: 0L
                else -> 0L
            }
            val newLikes = if (isLiked) currentLikes + 1 else maxOf(0L, currentLikes - 1)

            if (snapshot.exists()) {
                transaction.update(userRef, "totalLikes", newLikes)
            } else {
                val initialData = hashMapOf("totalLikes" to newLikes)
                transaction.set(userRef, initialData)
            }

            newLikes // Return the new like count
        }.addOnSuccessListener { newLikes ->
            _totalLikes.value = DataState.Success(newLikes)
        }.addOnFailureListener { e ->
            _totalLikes.value = DataState.Error(e.localizedMessage!!)
        }
    }
}