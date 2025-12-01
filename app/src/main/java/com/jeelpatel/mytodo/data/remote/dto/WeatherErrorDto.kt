package com.jeelpatel.mytodo.data.remote.dto

data class WeatherErrorDto(
    val error: ErrorInfo
)

data class ErrorInfo(
    val code: Int,
    val message: String
)