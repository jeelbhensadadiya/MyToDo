package com.jeelpatel.mytodo.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.model.UserRepository
import com.jeelpatel.mytodo.model.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class UserViewModel constructor(private val repository: UserRepository) : ViewModel() {

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message

    private val _user = MutableLiveData<UserEntity>()
    val user: LiveData<UserEntity> get() = _user
    private val _isRegistered = MutableLiveData<Boolean>()
    val isRegistered: LiveData<Boolean> get() = _isRegistered

    fun registerUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerUser(user)
            _message.postValue("User Registration Success")
            _isRegistered.postValue(true)
        }
    }

    fun loginUser(userEmail: String, userPassword: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val loggedInUser = repository.loginUser(userEmail, userPassword)
            if (loggedInUser != null) {
                _user.postValue(loggedInUser)
                _message.postValue("User Logged in Successfully")
                _isRegistered.postValue(true)

            } else {
                _message.postValue("Invalid email or password")
                _isRegistered.postValue(false)
            }
        }
    }
}