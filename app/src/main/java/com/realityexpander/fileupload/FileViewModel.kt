package com.realityexpander.fileupload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository() // should be injected in real app
) : ViewModel() {

    private val _downloadedFile = MutableLiveData<File?>()
    val downloadedFile = _downloadedFile as LiveData<File?>

    private val _error = MutableLiveData<String?>()
    val error = _error as LiveData<String?>

    fun uploadImage(file: File, fileName: String) {
        viewModelScope.launch {
            try {
                repository.uploadImage(file, fileName, "Chris Test")
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message
            }
        }
    }

    fun downloadImage(fileName: String) {
        viewModelScope.launch {
            try {
                _error.value = null
                _downloadedFile.value = repository.downloadImage(fileName)
            } catch (e: Exception) {
                e.printStackTrace()
                _downloadedFile.value = null
                _error.value = e.message
            }
        }
    }
}