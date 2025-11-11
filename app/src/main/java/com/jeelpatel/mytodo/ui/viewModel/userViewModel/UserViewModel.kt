package com.jeelpatel.mytodo.ui.viewModel.userViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.usecase.userUseCase.GetLoginUserUseCase
import com.jeelpatel.mytodo.domain.usecase.userUseCase.RegisterNewUserUseCase
import com.jeelpatel.mytodo.ui.viewModel.UserUiState
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val getLoginUser: GetLoginUserUseCase,
    private val registerNewUserUseCase: RegisterNewUserUseCase,
    private val sessionManager: SessionManager
) : ViewModel() {

    private var _uiState = MutableStateFlow<UserUiState>(UserUiState.Ideal)
    val uiState: StateFlow<UserUiState> = _uiState

    private var _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> get() = _isUserLoggedIn

    fun registerUser(
        userName: String,
        userEmail: String,
        userPassword: String,
        userRePassword: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            _uiState.value = UserUiState.Loading

            val result = registerNewUserUseCase(
                userName,
                userEmail,
                userPassword,
                userRePassword
            )

            result.onSuccess {
                _uiState.value = UserUiState.Success
            }.onFailure {
                _uiState.value = UserUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun loginUser(
        userEmail: String,
        userPassword: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = UserUiState.Loading
            val result = getLoginUser(userEmail, userPassword)

            result.onSuccess { loggedInUser ->
                _uiState.value = UserUiState.Success
                _isUserLoggedIn.value = true
                sessionManager.saveUserSession(loggedInUser.uId)
            }.onFailure {
                _uiState.value = UserUiState.Error(it.message ?: "Unknown error")
            }
        }
    }

    fun checkLoginStatus() {
        _isUserLoggedIn.value = sessionManager.isLoggedIn()
    }

    fun logoutUser() {
        sessionManager.clearSession()
        _isUserLoggedIn.value = false
        _uiState.value = UserUiState.Ideal
    }
}