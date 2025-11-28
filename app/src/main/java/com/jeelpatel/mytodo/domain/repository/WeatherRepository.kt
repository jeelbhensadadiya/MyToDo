package com.jeelpatel.mytodo.domain.repository

import com.jeelpatel.mytodo.domain.model.WeatherResponseModel
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getCurrentWeather(
        apiKey: String,
        location: String
    ): Flow<WeatherResponseModel>
}