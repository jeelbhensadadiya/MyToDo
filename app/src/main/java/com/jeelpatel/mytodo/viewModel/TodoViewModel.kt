package com.jeelpatel.mytodo.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.model.TodoRepository
import com.jeelpatel.mytodo.model.api.TodoItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(private val repository: TodoRepository) : ViewModel() {

    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos

    fun getTodos() {
        viewModelScope.launch {
            try {
                repository.getTodos()
                    .flowOn(Dispatchers.IO)
                    .collectLatest { todos ->
                        println("API Response Size: ${todos.size}")
                        _todos.value = todos
                    }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}