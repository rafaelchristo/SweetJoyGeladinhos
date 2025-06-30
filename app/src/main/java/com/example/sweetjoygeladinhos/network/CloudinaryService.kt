package com.example.sweetjoygeladinhos.network

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface CloudinaryService {

    @Multipart
    @POST("v1_1/sweetjoygeladinhos/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): Response<CloudinaryResponse>

    companion object {
        fun create(): CloudinaryService {
            return Retrofit.Builder()
                .baseUrl("https://api.cloudinary.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(CloudinaryService::class.java)
        }
    }
}

data class CloudinaryResponse(
    val secure_url: String
)
