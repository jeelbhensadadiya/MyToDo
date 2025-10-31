package com.jeelpatel.mytodo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeelpatel.mytodo.data.local.dao.TaskDao
import com.jeelpatel.mytodo.data.local.dao.UserDao
import com.jeelpatel.mytodo.data.local.entity.TaskEntity
import com.jeelpatel.mytodo.data.local.entity.UserEntity

@Database(entities = [UserEntity::class, TaskEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

}