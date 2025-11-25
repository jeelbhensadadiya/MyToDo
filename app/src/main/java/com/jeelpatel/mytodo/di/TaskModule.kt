package com.jeelpatel.mytodo.di

import com.jeelpatel.mytodo.domain.usecase.taskUseCase.CreateNewTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.DeleteTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetAllDeletedTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetCompletedTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetOverDueTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetPendingTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.GetTasksUseCase
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.RestoreTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import com.jeelpatel.mytodo.domain.usecase.taskUseCase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object TaskModule {

    @Provides
    fun provideTaskUseCases(
        getTasks: GetTasksUseCase,
        updateTask: UpdateTaskUseCase,
        createTask: CreateNewTaskUseCase,
        getCompleted: GetCompletedTaskUseCase,
        getPending: GetPendingTaskUseCase,
        getOverdue: GetOverDueTaskUseCase,
        delete: DeleteTaskUseCase,
        getDeleted: GetAllDeletedTaskUseCase,
        restore: RestoreTaskUseCase
    ) = TaskContainer(
        getTasks,
        updateTask,
        createTask,
        getCompleted,
        getPending,
        getOverdue,
        delete,
        getDeleted,
        restore
    )
}