package com.example.carbuddy.ui.fragments.auth

import android.content.Intent
import android.graphics.ColorSpace.Model
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.carbuddy.data.models.ModelUser
import com.example.carbuddy.preferences.PreferenceManager
import com.example.carbuddy.utils.DataState
import com.example.carbuddy.utils.DateTimeUtils
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VmAuth @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseFirestore,
    private val gso: GoogleSignInClient,
    private val storage: StorageReference,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _signUpStatus = MutableLiveData<DataState<Nothing>>()
    val signUpStatus: LiveData<DataState<Nothing>> = _signUpStatus

    private val _loginStatus = MutableLiveData<DataState<Nothing>>()
    val loginStatus: LiveData<DataState<Nothing>> = _loginStatus


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
        val fileName = DateTimeUtils.formatCompleteDateAndTime()
        val ref = storage.child("profile images/$fileName")
        ref.putFile(user.profileImageUri.toUri())
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    ref.downloadUrl.addOnSuccessListener { uri1 ->
                        user.profileImageUri = uri1.toString()
                        addUserToDb(user)
                    }
                } else {
                    _signUpStatus.value = DataState.Error(it.exception.toString())
                    throw it.exception!!
                }
            }
    }

    private fun addUserToDb(user: ModelUser) {
        db.collection("users").document(auth.currentUser?.uid!!).set(user)
            .addOnSuccessListener {
                preferenceManager.saveUserData(user)
                _signUpStatus.value = DataState.Success()
            }
            .addOnFailureListener {
                _signUpStatus.value = DataState.Error(it.message!!)
            }
    }


    //Login flow with email and password
    suspend fun loginWithEmailPass(email: String, password: String) {
        withContext(Dispatchers.Main) { _loginStatus.value = DataState.Loading }
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                val user = getUserFromDb()
                if (user != null) {
                    preferenceManager.saveUserData(user)
                }
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

    private fun getUserFromDb(): ModelUser? {
        var user: ModelUser? = null
        db.collection("users").document(auth.currentUser?.uid!!)
            .get()
            .addOnSuccessListener {
                user = it.toObject(ModelUser::class.java)
            }
            .addOnFailureListener {
                _loginStatus.value = DataState.Error(it.message!!)
            }
        return user
    }

    //Login flow with google
    fun signInWithGoogle(onIntentReady: (Intent) -> Unit) {
        _loginStatus.value = DataState.Loading
        val signInIntent = gso.signInIntent
        onIntentReady(signInIntent)
    }

    fun handleGoogleSignInResult(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account!!)
        } catch (e: ApiException) {
            _loginStatus.value = DataState.Error("Google sign in failed.")
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update LiveData
                    _loginStatus.value = DataState.Success()
                } else {
                    // If sign in fails, display a message to the user.
                    _loginStatus.value = DataState.Error("Authentication failed.")
                }
            }
    }
}