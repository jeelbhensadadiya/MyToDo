package com.jeelpatel.mytodo.domain.usecase.taskUseCase

import com.jeelpatel.mytodo.domain.repository.TaskRepository
import javax.inject.Inject

class RestoreTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(taskId: Int): Result<Unit> {
        if (taskId == 0) {
            return Result.failure(Exception("Unknown Error !!"))
        } else {
            repository.restoreTask(taskId)
            return Result.success(Unit)
        }
    }
}