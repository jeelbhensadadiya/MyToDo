package com.jeelpatel.mytodo.model

import com.jeelpatel.mytodo.model.local.dao.TaskDao
import com.jeelpatel.mytodo.model.local.entity.TaskEntity

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun createNewTask(task: TaskEntity) = taskDao.createTask(task)

    suspend fun getAllTask(currentUserId: Int): List<TaskEntity> = taskDao.tasksList(currentUserId)
}