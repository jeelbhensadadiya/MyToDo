package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetOverDueTaskUseCase @Inject constructor(private val repository: TaskRepository) {
    operator fun invoke(currentUserId: Int): Flow<List<TaskModel>> {
        return repository.overDueTask(currentUserId)
    }
}