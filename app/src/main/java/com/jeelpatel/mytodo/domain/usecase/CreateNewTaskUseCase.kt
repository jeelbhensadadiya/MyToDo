package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import javax.inject.Inject

class CreateNewTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel): Result<Unit> {

        if (taskModel.title.isEmpty()) {
            return Result.failure(Exception("Task title can not be empty !!"))

        } else {
            taskRepository.createNewTask(taskModel)
            return Result.success(Unit)
        }
    }
}