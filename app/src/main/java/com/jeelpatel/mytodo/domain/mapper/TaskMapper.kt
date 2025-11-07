package com.jeelpatel.mytodo.domain.mapper

import com.jeelpatel.mytodo.data.local.entity.TaskEntity
import com.jeelpatel.mytodo.domain.model.TaskModel

// map TaskModel to TaskEntity
fun TaskModel.toEntity(): TaskEntity {
    return TaskEntity(
        taskId = taskId,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = priority,
        dueDate = dueDate,
        createdAt = createdAt,
        userOwnerId = userOwnerId,
        isDeleted = isDeleted
    )
}


// map TaskEntity to TaskModel
fun TaskEntity.toDomain(): TaskModel {
    return TaskModel(
        taskId = taskId,
        title = title,
        description = description,
        isCompleted = isCompleted,
        priority = priority,
        dueDate = dueDate,
        createdAt = createdAt,
        userOwnerId = userOwnerId,
        isDeleted = isDeleted
    )
}