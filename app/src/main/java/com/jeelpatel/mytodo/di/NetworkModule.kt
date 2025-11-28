package com.jeelpatel.mytodo.di

import com.jeelpatel.mytodo.data.remote.api.ApiService
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
object NetworkModule {

    @Provides
    @Singleton
    @Named("TodoRetrofit")
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(Config.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    fun provideApiServices(@Named("TodoRetrofit") retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

}