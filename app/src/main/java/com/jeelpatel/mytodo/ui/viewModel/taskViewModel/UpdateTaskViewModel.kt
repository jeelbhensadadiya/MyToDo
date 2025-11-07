package com.jeelpatel.mytodo.ui.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateTaskViewModel @Inject constructor(
    private val useCases: TaskContainer
) : ViewModel() {

    private val _task = MutableStateFlow<List<TaskModel>>(emptyList())
    val task: StateFlow<List<TaskModel>> = _task.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    fun updateTaskStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.updateTask(taskId, isCompleted)
            _message.emit("Status Marked as ${if (isCompleted) "Completed" else "Incomplete"}")
        }
    }
}