package com.realityexpander.fileupload

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.realityexpander.fileupload.ui.theme.FileUploadTheme
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FileUploadTheme {

                val viewModel = viewModel<FileViewModel>()

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
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
                }

            }
        }
    }
}

