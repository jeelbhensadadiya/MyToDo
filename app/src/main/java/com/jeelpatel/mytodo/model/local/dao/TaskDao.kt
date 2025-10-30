package com.jeelpatel.mytodo.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jeelpatel.mytodo.model.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun createTask(task: TaskEntity)

    @Query("SELECT * FROM task_table WHERE userOwnerId = :currentUserId ORDER BY isCompleted DESC")
    fun tasksList(currentUserId: Int): Flow<List<TaskEntity>>

    @Query("UPDATE task_table SET isCompleted = :isCompleted WHERE taskId = :taskId")
    suspend fun updateTaskStatus(taskId: Int, isCompleted: Boolean)
}