package com.jeelpatel.mytodo.di

import com.jeelpatel.mytodo.domain.usecase.CreateNewTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.DeleteTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.GetAllDeletedTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.GetCompletedTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.GetOverDueTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.GetPendingTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.GetTasksUseCase
import com.jeelpatel.mytodo.domain.usecase.RestoreTaskUseCase
import com.jeelpatel.mytodo.domain.usecase.TaskContainer
import com.jeelpatel.mytodo.domain.usecase.UpdateTaskUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class TaskModule {

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