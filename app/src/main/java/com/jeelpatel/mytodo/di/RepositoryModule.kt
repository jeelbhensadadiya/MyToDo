package com.jeelpatel.mytodo.di

import com.jeelpatel.mytodo.data.repository.TaskRepositoryImpl
import com.jeelpatel.mytodo.data.repository.UserRepositoryImpl
import com.jeelpatel.mytodo.data.repository.WeatherRepositoryImpl
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import com.jeelpatel.mytodo.domain.repository.UserRepository
import com.jeelpatel.mytodo.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        impl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        impl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        impl: WeatherRepositoryImpl
    ): WeatherRepository
}