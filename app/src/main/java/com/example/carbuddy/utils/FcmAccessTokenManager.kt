package com.example.carbuddy.utils

import android.content.Context
import android.util.Log
import com.example.carbuddy.BuildConfig
import com.example.carbuddy.R
import com.google.auth.oauth2.GoogleCredentials
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.ByteArrayInputStream
import java.io.IOException
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class FcmAccessTokenManager @Inject constructor(@ApplicationContext val context: Context) {
    private val firebaseMessagingScope = "https://www.googleapis.com/auth/firebase.messaging"
    private val password = BuildConfig.PASSWORD_SERVICE_FILE

    fun getAccessToken(): String? {
        return try {

            val encryptedText = context.resources.getString(R.string.string_service_account)
            val decryptedText = EncryptionUtil.decrypt(encryptedText, password)

            val stream = ByteArrayInputStream(decryptedText.toByteArray(StandardCharsets.UTF_8))
            val googleCredentials =
                GoogleCredentials.fromStream(stream).createScoped(listOf(firebaseMessagingScope))

            googleCredentials.refresh()
            googleCredentials.accessToken.tokenValue
        } catch (e: IOException) {
            Log.d("access token error", e.localizedMessage ?: "Error occurred")
            null
        }
    }
}