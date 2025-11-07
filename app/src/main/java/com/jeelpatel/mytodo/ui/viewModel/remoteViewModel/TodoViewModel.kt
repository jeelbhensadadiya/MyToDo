package com.jeelpatel.mytodo.ui.viewModel.remoteViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.data.remote.dto.TodoDto
import com.jeelpatel.mytodo.data.repository.TodoRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepositoryImpl) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoDto>>(emptyList())
    val todos: StateFlow<List<TodoDto>> = _todos

    fun getTodos() {
        viewModelScope.launch {
            try {
                repository.getTodos()
                    .flowOn(Dispatchers.IO)
                    .collectLatest { todos ->
                        _todos.value = todos
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}