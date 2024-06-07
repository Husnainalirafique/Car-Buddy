package com.example.carbuddy.ui.fragments.auth

import android.content.Context
import android.graphics.Bitmap
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.carbuddy.data.models.ModelUser
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.ImageUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@HiltViewModel
class VmAuth @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val storage: StorageReference,
    private val preferenceManager: PreferenceManager,
    private val context: Context
) : ViewModel() {
    private val _signUpStatus = MutableLiveData<DataState<Nothing>>()
    val signUpStatus: LiveData<DataState<Nothing>> = _signUpStatus

    private val _loginStatus = MutableLiveData<DataState<Nothing>>()
    val loginStatus: LiveData<DataState<Nothing>> = _loginStatus

    private val _test = MutableLiveData<String>()
    val test: LiveData<String> = _test

    //Signup flow
    suspend fun signUpWithEmailPass(user: ModelUser) {
        withContext(Dispatchers.Main) { _signUpStatus.value = DataState.Loading }
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnSuccessListener {
                uploadProfileImage(user)
            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseAuthUserCollisionException) {
                    _signUpStatus.value = DataState.Error("User already exists.")
                } else {
                    _signUpStatus.value = DataState.Error("Authentication failed.")
                }
            }
    }

    private fun uploadProfileImage(user: ModelUser) {
        val ref = storage.child("profile_images/${System.currentTimeMillis()}")

        val bitmap = ImageUtils.uriToBitmap(context, user.profileImageUri.toUri())
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        ref.putBytes(data)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnSuccessListener { uri1 ->
                        user.profileImageUri = uri1.toString()
                        addUserToDb(user)
                    }
                } else {
                    _signUpStatus.value = DataState.Error(it.exception.toString())
                }
            }
    }

    private fun addUserToDb(user: ModelUser) {
        db.collection("users").document(auth.currentUser?.uid!!).set(user)
            .addOnSuccessListener {
                getUserFromDbAndSaveToPref()
            }
            .addOnFailureListener {
                _signUpStatus.value = DataState.Error(it.message!!)
            }
    }


    //Login flow with email and password
    fun loginWithEmailPass(email: String, password: String) {
        _loginStatus.value = DataState.Loading
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                getUserFromDbAndSaveToPref()
                _loginStatus.value = DataState.Success()
            }
            .addOnFailureListener { exception ->
                if (exception is FirebaseAuthInvalidCredentialsException) {
                    _loginStatus.value = DataState.Error("Wrong credentials Or not signed up.")
                } else {
                    _loginStatus.value = DataState.Error("Authentication failed.")
                }
            }
    }

    private fun getUserFromDbAndSaveToPref() {
        db.collection("users").document(auth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(ModelUser::class.java)
                if (user != null) {
                    val docId = it.id
                    user.docId = docId
                    preferenceManager.saveUserData(user)
                }
                _loginStatus.value = DataState.Success()
                _signUpStatus.value = DataState.Success()
            }
            .addOnFailureListener {
                _loginStatus.value = DataState.Error(it.message!!)
                _signUpStatus.value = DataState.Error(it.message!!)
            }
    }

}