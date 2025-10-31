package com.jeelpatel.mytodo.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.usecase.CreateNewTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.GetTasksUseCase
import com.jeelpatel.mytodo.domain.usecase.UpdateTaskUseCase
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
    private val getTasksUseCase: GetTasksUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val createNewTaskUseCase: CreateNewTaskUseCase,
) : ViewModel() {

    private val _task = MutableStateFlow<List<TaskModel>>(emptyList())
    val task: StateFlow<List<TaskModel>> = _task.asStateFlow()

    private val _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private val _isTaskCreated = MutableStateFlow(false)
    val isTaskCreated: StateFlow<Boolean> = _isTaskCreated

    fun createNewTask(task: TaskModel) {
        viewModelScope.launch(Dispatchers.IO) {
            createNewTaskUseCase.invoke(task)
            _message.emit("New Task Created")
            _isTaskCreated.value = true
        }
    }

    fun getAllTask(currentUserId: Int) {
        viewModelScope.launch {
            getTasksUseCase(currentUserId)
                .collect { taskList ->
                    _task.value = taskList
                }
        }
    }

    fun updateTaskStatus(taskId: Int, isCompleted: Boolean) {
        viewModelScope.launch {
            updateTaskUseCase.invoke(taskId, isCompleted)
            _message.emit("Status Marked as ${if (isCompleted) "Completed" else "Incomplete"}")
        }
    }
}