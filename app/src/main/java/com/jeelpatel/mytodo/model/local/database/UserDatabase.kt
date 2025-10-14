package com.jeelpatel.mytodo.model.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jeelpatel.mytodo.model.local.dao.TaskDao
import com.jeelpatel.mytodo.model.local.dao.UserDao
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import com.jeelpatel.mytodo.model.local.entity.UserEntity

@Database(entities = [UserEntity::class, TaskEntity::class], version = 1)
abstract class UserDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun taskDao(): TaskDao

}