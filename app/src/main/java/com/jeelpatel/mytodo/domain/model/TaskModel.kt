package com.jeelpatel.mytodo.domain.model

data class TaskModel(
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