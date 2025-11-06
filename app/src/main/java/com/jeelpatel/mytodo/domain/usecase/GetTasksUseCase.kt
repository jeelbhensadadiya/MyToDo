package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.TaskModel
import com.jeelpatel.mytodo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    operator fun invoke(userID: Int): Flow<List<TaskModel>> = flow {
        emit(taskRepository.getAllTask(userID).first())
    }
}