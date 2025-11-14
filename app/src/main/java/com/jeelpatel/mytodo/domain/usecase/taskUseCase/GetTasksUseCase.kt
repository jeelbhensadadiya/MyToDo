package com.jeelpatel.mytodo.domain.usecase.taskUseCase

import androidx.paging.PagingData
import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(userID: Int): Flow<PagingData<TaskModel>> {
        return taskRepository.getAllTask(userID)
    }
}