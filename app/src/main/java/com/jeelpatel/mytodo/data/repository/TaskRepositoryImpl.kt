package com.jeelpatel.mytodo.data.repository

import com.jeelpatel.mytodo.data.local.dao.TaskDao
import com.jeelpatel.mytodo.domain.mapper.toDomain
import com.jeelpatel.mytodo.domain.mapper.toEntity
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    override suspend fun createNewTask(task: TaskModel) {
        taskDao.createTask(task.toEntity())
    }

    override fun getAllTask(currentUserId: Int): Flow<List<TaskModel>> =
        taskDao.tasksList(currentUserId).map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean) =
        taskDao.updateTaskStatus(taskId, isCompleted)
}