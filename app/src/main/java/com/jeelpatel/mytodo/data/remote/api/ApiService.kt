package com.jeelpatel.mytodo.data.remote.api

import com.jeelpatel.mytodo.data.remote.dto.TodoDto
import com.jeelpatel.mytodo.utils.Config
import retrofit2.http.GET

interface ApiService {

    @GET(Config.GET_TODOS)
    suspend fun getTodos(): List<TodoDto>

}