package com.jeelpatel.mytodo.domain.usecase

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
