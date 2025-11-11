package com.jeelpatel.mytodo.ui.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import com.jeelpatel.mytodo.ui.viewModel.CreateTaskUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val useCases: TaskContainer,
) : ViewModel() {

    private var _uiState = MutableStateFlow<CreateTaskUiState>(CreateTaskUiState.Ideal)
    val uiState: StateFlow<CreateTaskUiState> = _uiState


    fun createNewTask(
        title: String,
        description: String,
        priority: Int,
        dueDate: String,
        isCompleted: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            // Start Loading
            _uiState.value = CreateTaskUiState.Loading

            // store result
            val result = useCases.createTask(
                title,
                description,
                priority,
                dueDate,
                isCompleted
            )

            result.onSuccess {
                _uiState.value = CreateTaskUiState.Success
            }.onFailure {
                _uiState.value = CreateTaskUiState.Error(it.message ?: "Unknown Error")
            }
        }
    }
}