package com.realityexpander.fileupload

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

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

    @GET("download")
    @Streaming
    suspend fun downloadImage(
        @Query("fileName") fileName: String
    ): Response<ResponseBody>

    companion object {
        // Replace with your own local IP for the server
        const val BASE_URL = "http://192.168.0.186:8080/"  // use trailing slash

        val okHttpClient = OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()

        val gson = GsonBuilder().setLenient().create()

        val instance by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(FileApi::class.java)
        }

    }
}

