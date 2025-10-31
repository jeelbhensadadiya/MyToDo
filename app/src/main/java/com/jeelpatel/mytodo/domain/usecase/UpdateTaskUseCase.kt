package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(taskId: Int, isCompleted: Boolean) {
        taskRepository.updateTaskStatus(taskId, isCompleted)
    }
}