package com.example.carbuddy.repositories

import android.content.Context
import com.example.carbuddy.preferences.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import javax.inject.Inject

class ServicesRepository @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val prefs: PreferenceManager,
    private val storage: StorageReference,
    private val context: Context
) {
}