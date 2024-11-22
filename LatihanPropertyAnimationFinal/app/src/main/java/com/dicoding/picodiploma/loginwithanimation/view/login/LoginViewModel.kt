package com.dicoding.picodiploma.loginwithanimation.view.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.Result
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _login = MutableLiveData<Result<LoginResponse>>()
    val login: LiveData<Result<LoginResponse>> = _login

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _login.value = Result.Loading

            when(val result = repository.login(email, password)) {
                is Result.Success -> {
                    val loginresponse = result.data
                    val user = UserModel(
                        email = email,
                        token = loginresponse.loginResult?.token.toString(),
                        isLogin = true
                    )
                    repository.saveSession(user)
                    _login.value = Result.Success(loginresponse)
                }
                is Result.Error -> {
                    _login.value = Result.Error(result.error)
                }
                Result.Loading ->{}
            }
        }
    }
}