package com.jeelpatel.mytodo.ui.viewModel

import com.jeelpatel.mytodo.domain.model.WeatherResponseModel

sealed class WeatherUiState {
    object Ideal : WeatherUiState()
    object Loading : WeatherUiState()
    data class Success(val weatherResponseModel: WeatherResponseModel) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}