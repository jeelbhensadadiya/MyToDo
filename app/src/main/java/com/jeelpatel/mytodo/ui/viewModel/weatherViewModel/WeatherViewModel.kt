package com.jeelpatel.mytodo.ui.viewModel.weatherViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeelpatel.mytodo.domain.usecase.weatherUseCase.GetCurrentWeatherUseCase
import com.jeelpatel.mytodo.ui.viewModel.WeatherException
import com.jeelpatel.mytodo.ui.viewModel.WeatherUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
            getCurrentWeatherUseCase(location)
                .catch { e ->
                    val message = when (e) {
                        is WeatherException.BadRequest -> e.message
                        is WeatherException.Unauthorized -> e.message
                        is WeatherException.NotFound -> e.message
                        is WeatherException.ServerError -> e.message
                        is WeatherException.NetworkError -> e.message
                        is WeatherException.Unknown -> e.message
                        else -> "Unknown Error.."
                    }
                    _weatherUiState.value = WeatherUiState.Error(message ?: "Something failed")
                    _weatherUiState.value = WeatherUiState.Ideal
                }
                .collectLatest { data ->
                    _weatherUiState.value = WeatherUiState.Success(data)
                }
        }
    }
}