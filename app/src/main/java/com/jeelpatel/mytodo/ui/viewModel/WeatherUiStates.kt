package com.jeelpatel.mytodo.ui.viewModel

import com.jeelpatel.mytodo.domain.model.WeatherResponseModel

sealed class WeatherUiState {
    object Ideal : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weatherResponseModel: WeatherResponseModel) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}


sealed class WeatherException(message: String) : Exception(message) {
    class BadRequest(message: String) : WeatherException(message)
    class Unauthorized(message: String) : WeatherException(message)
    class NotFound(message: String) : WeatherException(message)
    class ServerError(message: String) : WeatherException(message)
    class NetworkError(message: String) : WeatherException(message)
    class Unknown(message: String) : WeatherException(message)
}
