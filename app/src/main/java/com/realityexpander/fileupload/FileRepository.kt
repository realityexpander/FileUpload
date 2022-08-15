package com.realityexpander.fileupload

import kotlinx.coroutines.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
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

    // Download image
    //suspend fun downloadImage(fileName: String): File? {
    suspend fun downloadImage(fileName: String, callback: (tempFile: File?) -> Unit) {

//        var tempFile: File? = null
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                tempFile = File.createTempFile("temp", ".jpeg")
//
//                // download image from server and save it to temp file
//                val response = FileApi.instance.downloadImage(fileName)
//                response.byteStream().use { input ->
//                    tempFile?.outputStream().use { output ->
//                        if (output != null) {
//                            input.copyTo(output)
//                        }
//                    }
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//                tempFile = null
//            }
//        }.join()
//
//        return tempFile // return file or null


        CoroutineScope(Dispatchers.IO).launch {
            try {
                var tempFile: File? = null
                val defer = async(Dispatchers.IO) {
                    // Create a temp file in the cache directory
                    tempFile = File.createTempFile("temp", ".jpeg")

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

                        callback(tempFile)
                    }
                    false -> {
                        tempFile = null
                        callback(null)
                    }
                }
            } catch (e: CancellationException) {
                e.printStackTrace()
                callback(null)
            }
        }


//        return try {
//            // download image from server
//            FileApi.instance.downloadImage(fileName).byteStream().use { inputStream ->
//                // create temp file
//                val tempFile = File.createTempFile("temp", ".jpeg")
//                tempFile.outputStream().use { outputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//
//                tempFile
//            }
//        } catch (e: Exception) {
//            // handle exception
//            e.printStackTrace()
//            File("")
//        }
    }

    suspend fun downloadImage2(fileName: String): File? =
        withContext(Dispatchers.IO) {
            return@withContext try {
                var tempFile: File? = null
                val defer = async(Dispatchers.IO) {
                    // Create a temp file in the cache directory
                    tempFile = File.createTempFile("temp", ".jpeg")

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