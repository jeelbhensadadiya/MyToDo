package com.jeelpatel.mytodo.ui.viewModel

sealed class UserUiState {
    object Ideal : UserUiState()
    object Loading : UserUiState()
    object Success : UserUiState()
    data class Error(val message: String) : UserUiState()
}