package com.jeelpatel.mytodo.data.remote.api

import com.jeelpatel.mytodo.data.remote.dto.TodoDto
import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): List<TodoDto>
}