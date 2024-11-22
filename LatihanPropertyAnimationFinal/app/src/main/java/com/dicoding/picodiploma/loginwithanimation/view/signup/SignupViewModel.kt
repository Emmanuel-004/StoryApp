package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.Result
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.RegisterResponse
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {

    private val _signup = MutableLiveData<Result<RegisterResponse>>()
    val signup: LiveData<Result<RegisterResponse>> = _signup

    fun signup(name: String, email: String, password: String) {
        viewModelScope.launch {
            _signup.value = repository.register(name, email, password)
        }
    }
}