package com.jeelpatel.mytodo.ui.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
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
class FilterTaskViewModel @Inject constructor(private val useCases: TaskContainer) : ViewModel() {

    private val _task = MutableStateFlow<List<TaskModel>>(emptyList())
    val task: StateFlow<List<TaskModel>> = _task.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private var taskJob: Job? = null

    fun getAllTask(currentUserId: Int) {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getTasks(currentUserId)
                .collect {
                    _task.value = it
                }
        }
    }

    fun completedTask(currentUserId: Int) {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getCompleted(currentUserId).collect { completedTaskList ->
                _task.value = completedTaskList
            }
        }
    }

    fun pendingTask(currentUserId: Int) {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getPending(currentUserId)
                .collect { pendingTaskList ->
                    _task.value = pendingTaskList
                }
        }
    }

    fun overDueTask(currentUserId: Int) {
        taskJob?.cancel()
        taskJob = viewModelScope.launch {
            useCases.getOverdue(currentUserId)
                .collect { overDueTaskList ->
                    _task.value = overDueTaskList
                }
        }
    }
}