package com.jeelpatel.mytodo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.model.TaskRepository
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class TaskViewModel constructor(private val repository: TaskRepository) : ViewModel() {

    private val _task = MutableStateFlow<List<TaskEntity>>(emptyList())
    val task: StateFlow<List<TaskEntity>> = _task.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private val _isTaskCreated = MutableStateFlow(false)
    val isTaskCreated: StateFlow<Boolean> = _isTaskCreated

    fun createNewTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createNewTask(task)
            _message.emit("New Task Created")
            _isTaskCreated.value = true
        }
    }

    fun getAllTask(currentUserId: Int) {
        viewModelScope.launch {
            repository.getAllTask(currentUserId)
                .flowOn(Dispatchers.IO)
                .collect { task ->

                if (task.isEmpty()) {
                    _message.emit("No any task created")
                } else {
                    _task.value = task
                }
            }
        }
    }

    fun updateTaskStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, isCompleted)
            _message.emit("Status Marked as ${if (isCompleted) "Completed" else "Incomplete"}")
        }
    }
}