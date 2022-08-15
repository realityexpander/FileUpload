package com.realityexpander.fileupload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository() // should be injected in real app
) : ViewModel() {

    private val _downloadedFile = MutableLiveData<File?>()
    val downloadedFile = _downloadedFile as LiveData<File?>

    fun uploadImage(file: File, fileName: String) {
        viewModelScope.launch {
            repository.uploadImage(file, fileName, "Chris Test")
        }
    }

    fun downloadImage(fileName: String) {
        viewModelScope.launch {
//            repository.downloadImage(fileName) { file ->
//                // Must set the mutableLiveData value in the main thread
//                CoroutineScope(Dispatchers.Main).launch {
//                    _downloadedFile.value = file
//                }
//            }

            _downloadedFile.value = repository.downloadImage2(fileName)
        }
    }
}