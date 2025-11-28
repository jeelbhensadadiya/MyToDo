package com.jeelpatel.mytodo.di

import com.jeelpatel.mytodo.data.remote.api.WeatherService
import com.jeelpatel.mytodo.utils.Config
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Named
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object WeatherModule {

    // Weather
    @Provides
    @Singleton
    @Named("WeatherRetrofit")
    fun provideWeatherRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Config.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideWeatherServices(@Named("WeatherRetrofit") retrofit: Retrofit): WeatherService =
        retrofit.create(WeatherService::class.java)

}