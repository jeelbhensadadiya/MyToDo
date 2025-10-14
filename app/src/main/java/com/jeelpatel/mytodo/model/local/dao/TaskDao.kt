package com.jeelpatel.mytodo.model.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jeelpatel.mytodo.model.local.entity.TaskEntity

@Dao
interface TaskDao {

    @Insert
    suspend fun createTask(task: TaskEntity)

    @Query("SELECT * FROM task_table WHERE userOwnerId = :currentUserId")
    suspend fun tasksList(currentUserId: Int): List<TaskEntity>
}