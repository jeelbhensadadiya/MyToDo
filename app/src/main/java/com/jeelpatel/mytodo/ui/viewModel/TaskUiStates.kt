package com.jeelpatel.mytodo.ui.viewModel

import com.jeelpatel.mytodo.domain.model.TaskModel

sealed class TaskUiState {
    object Ideal : TaskUiState()
    object Loading : TaskUiState()
    object EmptyList : TaskUiState()
    data class Success(val tasks: List<TaskModel>) : TaskUiState()
    data class Error(val message: String) : TaskUiState()
}

sealed class CreateTaskUiState {
    object Ideal : CreateTaskUiState()
    object Loading : CreateTaskUiState()
    object Success : CreateTaskUiState()
    data class Error(val message: String) : CreateTaskUiState()
}