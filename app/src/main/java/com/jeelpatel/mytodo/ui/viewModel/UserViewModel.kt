package com.jeelpatel.mytodo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.usecase.GetLoginUserUseCase
import com.jeelpatel.mytodo.domain.usecase.RegisterNewUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getLoginUser: GetLoginUserUseCase,
    private val registerNewUserUseCase: RegisterNewUserUseCase,
) : ViewModel() {

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private val _user = MutableStateFlow<UserModel?>(null)
    val user: StateFlow<UserModel?> = _user

    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered

    fun registerUser(user: UserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            registerNewUserUseCase.invoke(user)
            _message.emit("User Registration Success")
            _isRegistered.value = true
        }
    }

    fun loginUser(userEmail: String, userPassword: String) {

        viewModelScope.launch {
            getLoginUser.invoke(userEmail, userPassword)
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