package com.jeelpatel.mytodo.model

import com.jeelpatel.mytodo.model.local.dao.TaskDao
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun createNewTask(task: TaskEntity) = taskDao.createTask(task)

    fun getAllTask(currentUserId: Int): Flow<List<TaskEntity>> = taskDao.tasksList(currentUserId)
}