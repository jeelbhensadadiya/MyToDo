package com.jeelpatel.mytodo.ui.viewModel.taskViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTaskViewModel @Inject constructor(
    private val useCases: TaskContainer,
) : ViewModel() {

    private var _message = MutableSharedFlow<String>()
    val message: SharedFlow<String> = _message

    private var _isTaskCreated = MutableStateFlow(false)
    val isTaskCreated: StateFlow<Boolean> = _isTaskCreated

    fun createNewTask(
        title: String,
        description: String,
        priority: Int,
        dueDate: String,
        isCompleted: Boolean
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val result = useCases.createTask(
                title,
                description,
                priority,
                dueDate,
                isCompleted
            )

            result.onSuccess {
                _message.emit("New task created")
                _isTaskCreated.value = true
            }.onFailure {
                _message.emit(it.message ?: "Unknown error")
                _isTaskCreated.value = false
            }
        }
    }
}