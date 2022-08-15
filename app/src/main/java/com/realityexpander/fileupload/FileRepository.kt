package com.realityexpander.fileupload

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.http.POST
import java.io.File

class FileRepository {

    suspend fun uploadImage(file: File, fileName: String, user: String): Boolean {
        return try {
            // upload image to server

            FileApi.instance.uploadImage(
                image = MultipartBody.Part
                    .createFormData(
                        "image_file",  // part.name
                        fileName, // original name of file on client side, on server is `part.originalFileName`
                        file.asRequestBody("image/jpeg".toMediaType())
                    ),
                description = MultipartBody.Part.createFormData("description", "image description"),
                user = MultipartBody.Part.createFormData("user", user),
                extraText = "extra text".toRequestBody("text/plan".toMediaType()),
                extraJson = "{\"key\": \"value\"}".toRequestBody("application/json".toMediaType())
            )

            true
        } catch (e: Exception) {
            // handle exception
            e.printStackTrace()

            false
        }


    }
}