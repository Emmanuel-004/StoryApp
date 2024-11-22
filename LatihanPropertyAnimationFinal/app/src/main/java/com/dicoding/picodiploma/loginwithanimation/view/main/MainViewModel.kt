package com.dicoding.picodiploma.loginwithanimation.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.local.StoryEntity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: UserRepository,
    private val storyRepository: StoryRepository) : ViewModel() {

    private val _stories = MutableLiveData<Result<List<StoryEntity>>>()
    val stories: LiveData<Result<List<StoryEntity>>> get()= _stories

    private val _isLogin = MutableLiveData<Boolean>()
    val isLogin: LiveData<Boolean> get()= _isLogin

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get()= _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get()= _errorMessage

    init {
        loginStatus()
        fetchStory()
    }

    private fun loginStatus() {
        viewModelScope.launch {
            _isLogin.value = repository.getSession().firstOrNull()?.token != null
        }

    }

    fun fetchStory() {
        _isLoading.value = true
        viewModelScope.launch {
            val token = repository.getSession().firstOrNull()?.token

            if (token != null) {
                _errorMessage.value = "token is null"
                _isLoading.value = false
                return@launch
            }
            try {
                val result = storyRepository.getAllStories("Bearer $token")
            } catch (e: Exception) {
                _errorMessage.value = e.message
            } finally {
                _isLoading.value = false

            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

}