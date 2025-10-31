package com.jeelpatel.mytodo.model

import com.jeelpatel.mytodo.model.local.dao.TaskDao
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TaskRepository(private val taskDao: TaskDao) {

    suspend fun createNewTask(task: TaskEntity) = taskDao.createTask(task)

    fun getAllTask(currentUserId: Int): Flow<List<TaskEntity>> = flow {
        emit(taskDao.tasksList(currentUserId))
    }

    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean) =
        taskDao.updateTaskStatus(taskId, isCompleted)
}