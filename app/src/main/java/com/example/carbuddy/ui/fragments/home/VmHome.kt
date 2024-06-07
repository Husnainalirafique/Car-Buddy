package com.example.carbuddy.ui.fragments.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carbuddy.data.models.ModelVendorProfile
import com.example.carbuddy.utils.DataState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VmHome @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore
) : ViewModel() {
    private val _mechanicsProfiles = MutableLiveData<DataState<List<ModelVendorProfile>>>()
    val mechanicsProfiles: LiveData<DataState<List<ModelVendorProfile>>> = _mechanicsProfiles

    init {
        fetchMechanicsProfiles()
    }

    private fun fetchMechanicsProfiles() {
        _mechanicsProfiles.value = DataState.Loading
        db.collection("mechanics").get()
            .addOnSuccessListener { documents ->
                val profiles = mutableListOf<ModelVendorProfile>()
                for (document in documents) {
                    document.toObject(ModelVendorProfile::class.java).let { profile ->
                        profiles.add(profile)
                    }
                }
                _mechanicsProfiles.value = DataState.Success(profiles)
            }
            .addOnFailureListener { exception ->
                _mechanicsProfiles.value = DataState.Error(exception.localizedMessage!!)
            }
    }

}