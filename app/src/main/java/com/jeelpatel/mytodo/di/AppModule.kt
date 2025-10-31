package com.jeelpatel.mytodo.di

import android.content.Context
import androidx.room.Room
import com.jeelpatel.mytodo.model.TaskRepository
import com.jeelpatel.mytodo.model.TodoRepository
import com.jeelpatel.mytodo.model.UserRepository
import com.jeelpatel.mytodo.model.api.ApiService
import com.jeelpatel.mytodo.model.local.dao.TaskDao
import com.jeelpatel.mytodo.model.local.dao.UserDao
import com.jeelpatel.mytodo.model.local.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "app_database"
        ).build()


    // Provide Dao's
    @Provides
    fun provideTaskDao(db: UserDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideUserDao(db: UserDatabase): UserDao = db.userDao()

    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    // Provide Repository
    @Provides
    @Singleton
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository = TaskRepository(taskDao)

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository = UserRepository(userDao)

    @Provides
    @Singleton
    fun provideTodoRepository(apiService: ApiService): TodoRepository = TodoRepository(apiService)

}