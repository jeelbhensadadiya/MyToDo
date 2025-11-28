package com.jeelpatel.mytodo.ui.viewModel.weatherViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.usecase.weatherUseCase.GetCurrentWeatherUseCase
import com.jeelpatel.mytodo.ui.viewModel.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {


    private var _weatherUiState = MutableStateFlow<WeatherUiState>(WeatherUiState.Ideal)
    val weatherUiState: StateFlow<WeatherUiState> = _weatherUiState


    fun getCurrentWeather(location: String) {
        viewModelScope.launch {
            getCurrentWeatherUseCase(location).collectLatest { weatherResponseModel ->
                _weatherUiState.value = WeatherUiState.Loading
                if (location.isEmpty()) {
                    _weatherUiState.value = WeatherUiState.Error("First enter your Location")
                } else {
                    _weatherUiState.value = WeatherUiState.Success(weatherResponseModel)
                }
            }
        }
    }
}