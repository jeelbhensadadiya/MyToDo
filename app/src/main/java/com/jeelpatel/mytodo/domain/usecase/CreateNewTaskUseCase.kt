package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import javax.inject.Inject

class CreateNewTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskModel: TaskModel) {
        taskRepository.createNewTask(taskModel)
    }
}