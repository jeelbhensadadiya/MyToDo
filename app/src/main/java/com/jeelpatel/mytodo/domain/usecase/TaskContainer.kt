package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.usecase.taskUseCase.CreateNewTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.DeleteTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetAllDeletedTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetCompletedTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetOverDueTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetPendingTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetTasksUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.RestoreTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.UpdateTaskUseCase

data class TaskContainer(
    val getTasks: GetTasksUseCase,
    val updateTask: UpdateTaskUseCase,
    val createTask: CreateNewTaskUseCase,
    val getCompleted: GetCompletedTaskUseCase,
    val getPending: GetPendingTaskUseCase,
    val getOverdue: GetOverDueTaskUseCase,
    val delete: DeleteTaskUseCase,
    val getDeleted: GetAllDeletedTaskUseCase,
    val restore: RestoreTaskUseCase
)
