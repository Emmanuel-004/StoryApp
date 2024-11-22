package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.local.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddNewStoryResponse
import com.dicoding.picodiploma.loginwithanimation.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.uriToFile
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryViewModel (private val repository: StoryRepository) : ViewModel() {
    private val _uploadResponse = MutableLiveData<Result<AddNewStoryResponse>>()
    val uploadResponse: LiveData<Result<AddNewStoryResponse>> = _uploadResponse

    private val _updateStory = MutableLiveData<Result<List<StoryEntity>>>()
    val updateStory: LiveData<Result<List<StoryEntity>>> = _updateStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadStory(imageUri: Uri?, description: String, token: String, context: Context) {
        imageUri?.let { uri ->
            viewModelScope.launch {
                val imageFile = uriToFile(uri, context).reduceFileImage()
                val descriptionBody = description.toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val multipartBody = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
                _isLoading.value = true
                val result = repository.uploadStory(multipartBody, descriptionBody, token)
                _uploadResponse.value = result

                if (result is Result.Success) {
                    fetchUpdatedStory(token)
                }
                _isLoading.value = false
            }
        }
    }
    private fun fetchUpdatedStory(token: String) {
        viewModelScope.launch {
            val result = repository.getAllStories(token)
            if (result is Result.Success) {
                _updateStory.value = Result.Success(result.data)
            }
        }
    }
}