package com.jeelpatel.mytodo.ui.viewModel.otherViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class ChannelViewModel : ViewModel() {

    private var _events = Channel<UiEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()


    fun onButtonClicked() {
        viewModelScope.launch {
            _events.send(UiEvent.ShowToast("Button clicked!"))
        }
    }


    fun navigateNext() {
        viewModelScope.launch {
            _events.send(UiEvent.NavigateToNextScreen)
        }
    }


    sealed class UiEvent {
        data class ShowToast(val message: String) : UiEvent()
        object NavigateToNextScreen : UiEvent()
    }
}