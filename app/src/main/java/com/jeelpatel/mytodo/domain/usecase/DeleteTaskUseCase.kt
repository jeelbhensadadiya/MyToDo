package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Int): Result<Unit> {
        return if (taskId == 0) {
            Result.failure(Exception("Unknown Error !!"))
        } else {
            repository.deleteTask(taskId)
            Result.success(Unit)
        }
    }
}