package com.jeelpatel.mytodo.model.api

import retrofit2.http.GET

interface ApiService {
    @GET("todos")
    suspend fun getTodos(): List<TodoItem>
}