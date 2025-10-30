package com.jeelpatel.mytodo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.model.UserRepository
import com.jeelpatel.mytodo.model.local.entity.UserEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private val _user = MutableStateFlow<UserEntity?>(null)
    val user: StateFlow<UserEntity?> = _user

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    fun registerUser(user: UserEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.registerUser(user)
            _message.emit("User Registration Success")
            _isRegistered.value = true
        }
    }

    fun loginUser(userEmail: String, userPassword: String) {

        viewModelScope.launch {
            repository.loginUser(userEmail, userPassword)
                .flowOn(Dispatchers.IO)
                .collect { loggedInUser ->

                    if (loggedInUser != null) {
                        _user.value = loggedInUser
                        _message.emit("User Logged in Successfully")
                        _isRegistered.value = true

                    } else {
                        _message.emit("Invalid email or password")
                        _isRegistered.value = false
                    }
                }
        }

    }
}