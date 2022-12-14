package com.realityexpander.fileupload

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.realityexpander.fileupload.ui.theme.FileUploadTheme
import java.io.File

// Server is here: https://github.com/realityexpander/KtorFileUpload

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileUploadTheme {

                val viewModel = viewModel<FileViewModel>()

                Column(
                    modifier = Modifier.fillMaxSize(),
                    Arrangement.SpaceAround,
                    Alignment.CenterHorizontally
                ) {
                    val downloadedFile by viewModel.downloadedFile.observeAsState()
                    val error by viewModel.error.observeAsState()

                    Button(onClick = {

                        // this would normally done in another class that has the application context injected by dagger hilt
                        val assetFileName = "chris in austin.jpg"

                        // Create a temp file in the cache directory
                        val file = File(cacheDir, "temp_file_for_upload.jpg")
                        file.createNewFile()
                        file.outputStream().use { fileOutputStream ->
                            assets
                                .open(assetFileName)
                                .copyTo(fileOutputStream)
                        }

                        viewModel.uploadImage(file, assetFileName)

                    }) {
                        Text("Upload Image")
                    }
                    Spacer(Modifier.height(16.dp))

                    // Download a file from our Ktor server
                    Button(onClick = {
                        // this would normally done in another class that has the application context injected by dagger hilt
                        viewModel.downloadImage("chrisyoung.jpeg")

                    }) {
                        Text("Download Image")
                    }

                    // Clear the cache
                    Button(onClick = {
                        // delete all the files in the cache directory(including the cache directory itself)
                        if(cacheDir.deleteRecursively()) {
                            // if successfully deleted all files then recreate the cache directory
                            cacheDir.mkdir()
                        }
                    }) {
                        Text("Clear Cache")
                    }

                    // Show the downloaded file
                    if(downloadedFile != null) {
                        Spacer(Modifier.height(16.dp))
                        AsyncImage(
                            model = downloadedFile,
                            modifier = Modifier.fillMaxSize(),
                            contentDescription = null
                        )
                    }

                    // Show error
                    error?.let { error ->
                        Spacer(Modifier.height(16.dp))
                        Text(error)
                    }
                }

            }
        }
    }
}

