package com.jeelpatel.mytodo.ui.viewModel.workerViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.jeelpatel.mytodo.service.WorkerExample
import com.jeelpatel.mytodo.ui.viewModel.WorkInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class WorkManagerExampleViewModel @Inject constructor(
    private val workManager: WorkManager
) : ViewModel() {


    private var _workInfoState = MutableStateFlow<WorkInfoState>(WorkInfoState.IDEAL)
    val workInfoState: StateFlow<WorkInfoState> = _workInfoState

    private var workId: UUID? = null


    fun startWorkManager() {
        viewModelScope.launch {

            val request = OneTimeWorkRequestBuilder<WorkerExample>()
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(
                            networkType = NetworkType.CONNECTED
                        )
                        .build()
                )
                .build()


            workId = request.id
            workManager.enqueue(request)


            observeWork()
        }
    }

    private suspend fun observeWork() {
        val id = workId ?: return

        workManager.getWorkInfoByIdFlow(id).collectLatest { info ->
            if (info == null) return@collectLatest

            _workInfoState.value =
                when (info.state) {
                    WorkInfo.State.ENQUEUED -> WorkInfoState.ENQUEUED
                    WorkInfo.State.RUNNING -> WorkInfoState.RUNNING
                    WorkInfo.State.SUCCEEDED -> WorkInfoState.SUCCEEDED
                    WorkInfo.State.FAILED -> WorkInfoState.FAILED
                    WorkInfo.State.CANCELLED -> WorkInfoState.CANCELLED
                    else -> WorkInfoState.IDEAL
                }
        }
    }
}