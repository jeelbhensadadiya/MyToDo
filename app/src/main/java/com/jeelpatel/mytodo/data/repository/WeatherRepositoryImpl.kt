package com.jeelpatel.mytodo.data.repository

import com.jeelpatel.mytodo.data.remote.api.WeatherService
import com.jeelpatel.mytodo.domain.mapper.toDomain
import com.jeelpatel.mytodo.domain.model.WeatherResponseModel
import com.jeelpatel.mytodo.domain.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val service: WeatherService) :
    WeatherRepository {

    override fun getCurrentWeather(
        apiKey: String,
        location: String
    ): Flow<WeatherResponseModel> =
        flow {
            emit(service.getCurrentWeather(apiKey, location).toDomain())
        }.flowOn(Dispatchers.IO)
}