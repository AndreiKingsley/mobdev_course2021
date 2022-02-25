package com.example.chinchopaapp.sign_up

import androidx.lifecycle.viewModelScope
import com.example.chinchopaapp.BaseViewModel
import com.example.chinchopaapp.interactor.SignUpInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    signUpInteractor: SignUpInteractor
) : BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    data class SignUpData(
        val firstname: String,
        val lastname: String,
        val nickname: String,
        val email: String,
        val password: String
    )

    fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                val signUpData = SignUpData(
                    firstname,
                    lastname,
                    nickname,
                    email,
                    password
                )
                Timber.d("EMAIL!!!")
                // TODO
                // _eventChannel.send(Event.SignUpSuccess)
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            } catch (error: Exception) {
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            }
        }
    }

    sealed class Event {
        object SignUpSuccess : Event()
        object SignUpEmailConfirmationRequired : Event()
    }
}
