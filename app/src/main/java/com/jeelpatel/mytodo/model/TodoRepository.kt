package com.jeelpatel.mytodo.model

import com.jeelpatel.mytodo.model.api.ApiService
import com.jeelpatel.mytodo.model.api.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoRepository @Inject constructor(private val apiService: ApiService) {
    fun getTodos(): Flow<List<TodoItem>> = flow {
        emit(apiService.getTodos())
    }.flowOn(Dispatchers.IO)
}