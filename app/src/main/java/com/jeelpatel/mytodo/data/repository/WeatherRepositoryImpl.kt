package com.jeelpatel.mytodo.data.repository

import com.jeelpatel.mytodo.data.remote.api.WeatherService
import com.jeelpatel.mytodo.domain.mapper.toDomain
import com.jeelpatel.mytodo.domain.model.WeatherResponseModel
import com.jeelpatel.mytodo.domain.repository.WeatherRepository
import com.jeelpatel.mytodo.ui.viewModel.WeatherException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.net.UnknownHostException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val service: WeatherService) :
    WeatherRepository {

    override fun getCurrentWeather(
        apiKey: String,
        location: String
    ): Flow<WeatherResponseModel> =

        flow {
            try {
                val response = service.getCurrentWeather(apiKey, location)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        emit(body.toDomain())
                    } else {
                        throw WeatherException.Unknown("Empty Response from Server")
                    }
                } else {
                    when (response.code()) {
                        400 -> throw WeatherException.BadRequest("Invalid location format.")
                        401 -> throw WeatherException.Unauthorized("Invalid API key.")
                        403 -> throw WeatherException.NotFound("API key has exceeded calls per month quota.")
                        404 -> throw WeatherException.NotFound("Weather data not found.")
                    }
                }
            } catch (e: UnknownHostException) {
                throw WeatherException.NetworkError("No internet connection")
            } catch (e: java.net.SocketTimeoutException) {
                throw WeatherException.NetworkError("Connection timed out")
            } catch (e: Exception) {
                throw WeatherException.Unknown(e.message ?: "Unexpected error")
            }

        }.flowOn(Dispatchers.IO)
}