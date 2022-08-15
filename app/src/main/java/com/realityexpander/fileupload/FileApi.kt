package com.realityexpander.fileupload

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface FileApi {

    @Multipart
    @POST("image")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part description: MultipartBody.Part,
        @Part user: MultipartBody.Part,
        @Part("extraText") extraText: RequestBody,  // RequestBody must be named
        @Part("extraJson") extraJson: RequestBody
//        @Part user_id: MultipartBody.Part,
//        @Part user_email: MultipartBody.Part,
//        @Part user_phone: MultipartBody.Part,
//        @Part user_address: MultipartBody.Part,
//        @Part user_city: MultipartBody.Part,
//        @Part user_state: MultipartBody.Part,
//        @Part user_zip: MultipartBody.Part,
//        @Part user_country: MultipartBody.Part,
//        @Part user_lat: MultipartBody.Part,
//        @Part user_lng: MultipartBody.Part,
//        @Part user_ip: MultipartBody.Part
    )

    companion object {
        // Replace with your own local IP for the server
        const val BASE_URL = "http://192.168.0.186:8080/"  // use trailing slash

        val instance by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build()
                .create(FileApi::class.java)
        }

    }
}

