package com.example.carbuddy.data.remote

import com.example.carbuddy.data.Message
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface FcmApi {

    @POST("v1/projects/car-buddy-e89b4/messages:send")
    suspend fun sendNotification(
        @Header("Content-Type") type: String = "application/json",
        @Header("Authorization") authorization: String,
        @Body notification: Message
    ): Response<ResponseBody>
}