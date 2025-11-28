package com.jeelpatel.mytodo.domain.usecase.weatherUseCase

import com.jeelpatel.mytodo.domain.model.WeatherResponseModel
import com.jeelpatel.mytodo.domain.repository.WeatherRepository
import com.jeelpatel.mytodo.utils.Config
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
    operator fun invoke(location: String): Flow<WeatherResponseModel> {
        return repository.getCurrentWeather(
            apiKey = Config.WEATHER_API_KEY,
            location = location
        )
    }
}