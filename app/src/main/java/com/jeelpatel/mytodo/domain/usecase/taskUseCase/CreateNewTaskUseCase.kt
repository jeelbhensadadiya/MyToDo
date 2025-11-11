package com.jeelpatel.mytodo.domain.usecase.taskUseCase

import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import com.jeelpatel.mytodo.utils.SessionManager
import com.jeelpatel.mytodo.utils.UiHelper
import javax.inject.Inject

class CreateNewTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val sessionManager: SessionManager
) {

    suspend operator fun invoke(
        title: String,
        description: String,
        priority: Int,
        dueDate: String,
        isCompleted: Boolean
    ): Result<Unit> {


        // check title field
        if (title.isBlank()) {
            return Result.failure(Exception("Title cannot be empty"))
        }


        // check description field
        if (description.isBlank()) {
            return Result.failure(Exception("Description cannot be empty"))
        }


        // check due date field
        if (dueDate.isBlank()) {
            return Result.failure(Exception("Due date required"))
        }


        // store task data into TaskModel
        val taskModel = TaskModel(
            title = title,
            description = description,
            isCompleted = isCompleted,
            priority = priority,
            dueDate = UiHelper.parseDateToMillis(dueDate),
            createdAt = System.currentTimeMillis(),
            userOwnerId = sessionManager.getUserId(),
            isDeleted = false
        )


        // return data or errors
        return try {
            taskRepository.createNewTask(taskModel)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}