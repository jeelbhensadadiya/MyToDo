package com.jeelpatel.mytodo.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.jeelpatel.mytodo.data.local.dao.TaskDao
import com.jeelpatel.mytodo.domain.mapper.toDomain
import com.jeelpatel.mytodo.domain.mapper.toEntity
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {

    // create new task
    override suspend fun createNewTask(task: TaskModel) {
        taskDao.createTask(task.toEntity())
    }


    // get All tasks
    override fun getAllTask(currentUserId: Int): Flow<PagingData<TaskModel>> =
        Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                taskDao.tasksList(currentUserId)
            }
        ).flow.map { pagingData ->
            pagingData.map { it.toDomain() }
        }


    // get all Deleted tasks
    override fun getAllDeletedTask(currentUserId: Int): Flow<List<TaskModel>> =
        taskDao.getDeletedTasks(currentUserId).map { deleteTask ->
            deleteTask.map { it.toDomain() }
        }


    // get Completed tasks
    override fun completedTask(currentUserId: Int): Flow<List<TaskModel>> =
        taskDao.completedTasks(currentUserId).map { task ->
            task.map { it.toDomain() }
        }


    // get Pending tasks
    override fun pendingTask(currentUserId: Int): Flow<List<TaskModel>> =
        taskDao.pendingTasks(currentUserId).map { task ->
            task.map { it.toDomain() }
        }


    // get OverDue tasks
    override fun overDueTask(currentUserId: Int): Flow<List<TaskModel>> =
        taskDao.overdueTasks(currentUserId).map { task ->
            task.map { it.toDomain() }
        }


    // update task status (isCompleted)
    override suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean) =
        taskDao.updateTaskStatus(taskId, isCompleted)


    // delete task
    override suspend fun deleteTask(taskId: Int) =
        taskDao.deleteTaskById(taskId)


    // restore deleted task
    override suspend fun restoreTask(taskId: Int) {
        taskDao.restoreTask(taskId)
    }

}