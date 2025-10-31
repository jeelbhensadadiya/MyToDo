package com.jeelpatel.mytodo.data.repository

import com.jeelpatel.mytodo.data.remote.api.ApiService
import com.jeelpatel.mytodo.data.remote.dto.TodoDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(private val apiService: ApiService) {
    fun getTodos(): Flow<List<TodoDto>> = flow {
        emit(apiService.getTodos())
    }.flowOn(Dispatchers.IO)
}