package com.jeelpatel.mytodo.data.remote.api

import com.jeelpatel.mytodo.data.remote.dto.WeatherResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("aqi") aqi: String = "yes",
    ): Response<WeatherResponseDto>
}