package com.jeelpatel.mytodo.domain.usecase.weatherUseCase

import com.jeelpatel.mytodo.domain.repository.WeatherRepository
import com.jeelpatel.mytodo.utils.Config
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(private val repository: WeatherRepository) {
    operator fun invoke(location: String) = repository.getCurrentWeather(
        apiKey = Config.WEATHER_API_KEY,
        location = location
    )
}