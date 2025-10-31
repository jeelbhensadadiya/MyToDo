package com.jeelpatel.mytodo.data.remote.dto

data class TodoDto(
    val userId: Int,
    val id: Int,
    val title: String,
    val completed: Boolean
)