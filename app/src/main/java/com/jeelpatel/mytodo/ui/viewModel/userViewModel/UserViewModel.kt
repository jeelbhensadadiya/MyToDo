package com.jeelpatel.mytodo.ui.viewModel.userViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.usecase.userUseCase.GetLoginUserUseCase
import com.jeelpatel.mytodo.domain.usecase.userUseCase.RegisterNewUserUseCase
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

    private val _isUserCreated = MutableStateFlow(false)
    val isUserCreated: StateFlow<Boolean> = _isUserCreated

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn

    fun registerUser(
        userName: String,
        userEmail: String,
        userPassword: String,
        userRePassword: String
    ) {

        viewModelScope.launch(Dispatchers.IO) {
            val result = registerNewUserUseCase(
                userName,
                userEmail,
                userPassword,
                userRePassword
            )

            result.onSuccess {
                _message.emit("User Registration Success")
                _isUserCreated.value = true
            }.onFailure {
                _message.emit(it.message ?: "Unknown error")
                _isUserCreated.value = false
            }
        }
    }

    fun loginUser(
        userEmail: String,
        userPassword: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = getLoginUser(userEmail, userPassword)

            result.onSuccess { loggedInUser ->
                _user.value = loggedInUser
                _isUserLoggedIn.value = true
            }.onFailure {
                _message.emit(it.message ?: "Unknown error")
                _isUserLoggedIn.value = false
            }
        }
    }
}