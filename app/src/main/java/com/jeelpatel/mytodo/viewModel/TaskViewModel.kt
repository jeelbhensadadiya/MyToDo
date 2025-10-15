package com.jeelpatel.mytodo.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.model.TaskRepository
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskViewModel constructor(private val repository: TaskRepository) : ViewModel() {

    private val _task = MutableLiveData<List<TaskEntity>>()
    val task: LiveData<List<TaskEntity>> = _task

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isTaskCreated = MutableLiveData<Boolean>()
    val isTaskCreated: LiveData<Boolean> = _isTaskCreated

    fun createNewTask(task: TaskEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createNewTask(task)
            _message.postValue("New Task Created")
            _isTaskCreated.postValue(true)
        }
    }

    fun getAllTask(currentUserId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val task = repository.getAllTask(currentUserId)
            if (task.isEmpty()) {
                _message.postValue("No any task created")
            } else {
                _task.postValue(task)
            }
        }
    }
}