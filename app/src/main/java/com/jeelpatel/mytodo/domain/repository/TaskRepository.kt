package com.jeelpatel.mytodo.domain.repository

import com.jeelpatel.mytodo.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createNewTask(task: TaskModel)


    fun getAllTask(currentUserId: Int): Flow<List<TaskModel>>


    fun getAllDeletedTask(currentUserId: Int): Flow<List<TaskModel>>


    fun completedTask(currentUserId: Int): Flow<List<TaskModel>>


    fun pendingTask(currentUserId: Int): Flow<List<TaskModel>>


    fun overDueTask(currentUserId: Int): Flow<List<TaskModel>>


    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)


    suspend fun deleteTask(taskId: Int)


    suspend fun restoreTask(taskId: Int)
}