package com.jeelpatel.mytodo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class TaskViewModel @Inject constructor(
    private val useCases: TaskContainer
) : ViewModel() {

    private val _task = MutableStateFlow<List<TaskModel>>(emptyList())
    val task: StateFlow<List<TaskModel>> = _task.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private val _isTaskCreated = MutableStateFlow(false)
    val isTaskCreated: StateFlow<Boolean> = _isTaskCreated

    fun createNewTask(task: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = useCases.createTask(task)
            if (result.isFailure) {
                _message.emit(result.exceptionOrNull()?.message ?: "Unknown error !!")
                _isTaskCreated.value = false
            } else if (result.isSuccess) {
                _message.emit("New Task Created")
                _isTaskCreated.value = true
            }
        }
    }

    fun getAllTask(currentUserId: Int) {
        viewModelScope.launch {
            useCases.getTasks(currentUserId)
                .collect {
                    _task.value = it
                }
        }
    }

    fun getAllDeletedTask(currentUserId: Int) {
        viewModelScope.launch {
            useCases.getDeleted(currentUserId)
                .collect {
                    _task.value = it
                }
        }
    }

    fun completedTask(currentUserId: Int) {
        viewModelScope.launch {
            useCases.getCompleted(currentUserId).collect { completedTaskList ->
                _task.value = completedTaskList
            }
        }
    }

    fun pendingTask(currentUserId: Int) {
        viewModelScope.launch {
            useCases.getPending(currentUserId)
                .collect { pendingTaskList ->
                    _task.value = pendingTaskList
                }
        }
    }

    fun overDueTask(currentUserId: Int) {
        viewModelScope.launch {
            useCases.getOverdue(currentUserId)
                .collect { overDueTaskList ->
                    _task.value = overDueTaskList
                }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            val result = useCases.delete(taskId)
            if (result.isFailure) {
                _message.emit(result.exceptionOrNull()?.message ?: "Unknown Error !!")
            } else {
                _message.emit("Task Deleted")
            }
        }
    }

    fun restoreTask(taskId: Int) {
        viewModelScope.launch {
            val result = useCases.restore(taskId)
            if (result.isFailure) {
                _message.emit(result.exceptionOrNull()?.message ?: "Unknown Error !!")
            } else {
                _message.emit("Task Restored Successfully")
            }
        }
    }

    fun updateTaskStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.updateTask(taskId, isCompleted)
            _message.emit("Status Marked as ${if (isCompleted) "Completed" else "Incomplete"}")
        }
    }
}