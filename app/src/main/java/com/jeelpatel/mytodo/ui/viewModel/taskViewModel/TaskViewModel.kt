package com.jeelpatel.mytodo.ui.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val useCases: TaskContainer,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _task = MutableStateFlow<List<TaskModel>>(emptyList())
    val task: StateFlow<List<TaskModel>> = _task.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private var taskJob: Job? = null
    private val currentUserId = sessionManager.getUserId()


    // Filter for get All tasks
    fun getAllTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getTasks(currentUserId)
                .collect {
                    _task.value = it
                }
        }
    }


    // Filter for get all completed tasks
    fun completedTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getCompleted(currentUserId).collect { completedTaskList ->
                _task.value = completedTaskList
            }
        }
    }


    // Filter for get all pending tasks
    fun pendingTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getPending(currentUserId)
                .collect { pendingTaskList ->
                    _task.value = pendingTaskList
                }
        }
    }


    // Filter for get all overdue tasks
    fun overDueTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getOverdue(currentUserId)
                .collect { overDueTaskList ->
                    _task.value = overDueTaskList
                }
        }
    }


    // Get all deleted tasks for Recycler Bin
    fun getAllDeletedTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getDeleted(currentUserId)
                .collect {
                    _task.value = it
                }
        }
    }


    // Delete or move task to recycler Bin
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


    // Restore task from recycler bin
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


    // Update task status (Completed or Not Completed)
    fun updateTaskStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.updateTask(taskId, isCompleted)
            _message.emit("Status Marked as ${if (isCompleted) "Completed" else "Incomplete"}")
        }
    }
}