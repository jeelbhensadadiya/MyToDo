package com.jeelpatel.mytodo.domain.repository

import com.jeelpatel.mytodo.domain.model.TaskModel
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createNewTask(task: TaskModel)

    fun getAllTask(currentUserId: Int): Flow<List<TaskModel>>

    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)
}