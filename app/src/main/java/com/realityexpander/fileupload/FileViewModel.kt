package com.realityexpander.fileupload

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.File

class FileViewModel(
    private val repository: FileRepository = FileRepository() // should be injected in real app
) : ViewModel() {

    fun uploadImage(file: File, fileName: String) {
        viewModelScope.launch {
            repository.uploadImage(file, fileName, "Chris Test")
        }
    }
}