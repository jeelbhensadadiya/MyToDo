package com.jeelpatel.mytodo.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "task_table",
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["uId"],
        childColumns = ["userOwnerId"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int = 0,
    val title: String,
    val description: String?,
    val isCompleted: Boolean,
    val priority: Int,
    val dueDate: Long,
    val createdAt: Long = System.currentTimeMillis(),
    val userOwnerId: Int,
    val isDeleted: Boolean = false
)