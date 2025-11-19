package com.jeelpatel.mytodo.ui.viewModel

sealed class WorkInfoState {
    object IDEAL : WorkInfoState()
    object ENQUEUED : WorkInfoState()
    object RUNNING : WorkInfoState()
    object SUCCEEDED : WorkInfoState()
    object FAILED : WorkInfoState()
    object CANCELLED : WorkInfoState()
}