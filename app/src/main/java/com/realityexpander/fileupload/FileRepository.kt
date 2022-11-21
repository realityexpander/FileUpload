package com.realityexpander.fileupload

import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class FileRepository {

    // upload image to server
    suspend fun uploadImage(file: File, fileName: String, user: String): Boolean {
        return try {
            FileApi.instance.uploadImage(
                image = MultipartBody.Part
                    .createFormData(
                        "image_file",  // part.name for this @Part
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


    // Download image from server
    suspend fun downloadImage(fileName: String): File? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                var tempFile: File? = null
                val defer = async(Dispatchers.IO) {
                    // Create a temp file in the cache directory (could also download permanently to `file` directory)
                    tempFile = File.createTempFile("temp-$fileName-", ".jpeg")

                    // download image from server
                    FileApi.instance.downloadImage(fileName)
                }

                val response = defer.await()
                when (response.isSuccessful) {
                    true  -> {
                        // Save downloaded file to temp file in cache directory
                        response.body()?.byteStream()?.use { input ->
                            tempFile?.outputStream().use { output ->
                                if (output != null) {
                                    input.copyTo(output)
                                }
                            }
                        }

                        tempFile
                    }
                    false -> {
                        null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()

                null
            }
        }
}