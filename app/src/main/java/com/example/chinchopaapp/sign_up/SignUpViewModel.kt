package com.example.chinchopaapp.sign_up

import androidx.lifecycle.viewModelScope
import com.example.chinchopaapp.AuthRepository
import com.example.chinchopaapp.BaseViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class SignUpViewModel : BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            // TODO
            /*
            try {
                AuthRepository.signUp(
                    firstname,
                    lastname,
                    nickname,
                    email,
                    password
                )
                // _eventChannel.send(Event.SignUpSuccess)
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            } catch (error: Exception) {
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            }

             */
        }
    }

    sealed class Event {
        object SignUpSuccess : Event()
        object SignUpEmailConfirmationRequired : Event()
    }
}
