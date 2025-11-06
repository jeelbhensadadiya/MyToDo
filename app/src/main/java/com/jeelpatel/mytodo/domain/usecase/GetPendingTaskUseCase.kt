package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPendingTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(currentUserId: Int): Flow<List<TaskModel>> = flow {
        emit(repository.pendingTask(currentUserId).first())
    }
}