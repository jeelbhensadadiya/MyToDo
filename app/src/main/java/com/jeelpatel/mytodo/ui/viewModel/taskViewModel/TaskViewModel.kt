package com.jeelpatel.mytodo.ui.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import com.jeelpatel.mytodo.ui.viewModel.TaskUiState
import com.jeelpatel.mytodo.utils.SessionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val useCases: TaskContainer,
    private val sessionManager: SessionManager
) : ViewModel() {

    private var _uiState = MutableStateFlow<TaskUiState>(TaskUiState.Ideal)
    val uiStates: StateFlow<TaskUiState> = _uiState

    private var taskJob: Job? = null
    private val currentUserId get() = sessionManager.getUserId()


    // Filter for get All tasks
    fun getAllTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getTasks(currentUserId)
                .collect {
                    _uiState.value = TaskUiState.Success(it)
                }
        }
    }


    // Filter for get all completed tasks
    fun completedTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getCompleted(currentUserId).collect {
                _uiState.value = TaskUiState.Success(it)
            }
        }
    }


    // Filter for get all pending tasks
    fun pendingTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getPending(currentUserId)
                .collect {
                    _uiState.value = TaskUiState.Success(it)
                }
        }
    }


    // Filter for get all overdue tasks
    fun overDueTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getOverdue(currentUserId)
                .collect {
                    _uiState.value = TaskUiState.Success(it)
                }
        }
    }


    // Get all deleted tasks for Recycler Bin
    fun getAllDeletedTask() {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getDeleted(currentUserId)
                .collect {
                    _uiState.value = TaskUiState.Success(it)
                }
        }
    }


    // Delete or move task to recycler Bin
    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            val result = useCases.delete(taskId)
            result.onFailure {
                _uiState.value =
                    TaskUiState.Error(it.message ?: "Unknown error")
            }
        }
    }


    // Restore task from recycler bin
    fun restoreTask(taskId: Int) {
        viewModelScope.launch {
            val result = useCases.restore(taskId)
            result.onFailure {
                _uiState.value =
                    TaskUiState.Error(it.message ?: "Unknown error")
            }
        }
    }


    // Update task status (Completed or Not Completed)
    fun updateTaskStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            useCases.updateTask(taskId, isCompleted)
        }
    }
}