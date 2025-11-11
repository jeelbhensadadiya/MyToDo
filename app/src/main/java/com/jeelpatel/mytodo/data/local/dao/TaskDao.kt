package com.jeelpatel.mytodo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeelpatel.mytodo.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTask(task: TaskEntity)


    @Query(
        """
        SELECT *
        FROM task_table
        WHERE userOwnerId = :currentUserId AND isDeleted = 0
        ORDER BY dueDate DESC
        """
    )
    fun tasksList(currentUserId: Int): Flow<List<TaskEntity>>


    @Query(
        """
        SELECT *
        FROM task_table
        WHERE userOwnerId = :currentUserId AND isCompleted = 1 AND isDeleted = 0
        ORDER BY dueDate DESC
        """
    )
    fun completedTasks(currentUserId: Int): Flow<List<TaskEntity>>


    @Query(
        """
        SELECT *
        FROM task_table
        WHERE userOwnerId = :currentUserId AND isCompleted = 0 AND isDeleted = 0
        ORDER BY dueDate DESC
        """
    )
    fun pendingTasks(currentUserId: Int): Flow<List<TaskEntity>>


    @Query(
        """
        SELECT *
        FROM task_table
        WHERE userOwnerId = :currentUserId AND isCompleted = 0 AND isDeleted = 0 AND dueDate < :currentTime
        ORDER BY dueDate ASC
        """
    )
    fun overdueTasks(
        currentUserId: Int, currentTime: Long = System.currentTimeMillis()
    ): Flow<List<TaskEntity>>


    @Query(
        """
        UPDATE task_table
        SET isDeleted = 1
        WHERE taskId = :taskId
        """
    )
    suspend fun deleteTaskById(taskId: Int)


    @Query(
        """
        SELECT * FROM task_table 
        WHERE isDeleted = 1 AND userOwnerId = :userId
        """
    )
    fun getDeletedTasks(userId: Int): Flow<List<TaskEntity>>


    @Query(
        """
        UPDATE task_table
        SET isDeleted = 0
        WHERE taskId = :taskId
        """
    )
    suspend fun restoreTask(taskId: Int)


    @Query(
        """
        UPDATE task_table
        SET isCompleted = :isCompleted
        WHERE taskId = :taskId
        """
    )
    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)
}