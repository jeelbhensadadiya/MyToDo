package com.jeelpatel.mytodo.di

import android.content.Context
import androidx.room.Room
import com.jeelpatel.mytodo.data.local.dao.TaskDao
import com.jeelpatel.mytodo.data.local.dao.UserDao
import com.jeelpatel.mytodo.data.local.database.UserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): UserDatabase =
        Room.databaseBuilder(
            context,
            UserDatabase::class.java,
            "app_database"
        ).build()

    @Provides
    fun provideTaskDao(db: UserDatabase): TaskDao = db.taskDao()

    @Provides
    fun provideUserDao(db: UserDatabase): UserDao = db.userDao()
}