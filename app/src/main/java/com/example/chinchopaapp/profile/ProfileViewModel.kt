package com.example.chinchopaapp.profile

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.example.chinchopaapp.BaseViewModel
import com.example.chinchopaapp.entity.User
import com.example.chinchopaapp.interactor.AuthInteractor
import com.example.chinchopaapp.interactor.UsersInteractor
import com.example.chinchopaapp.users.UserListViewModel
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
    private val usersInteractor: UsersInteractor
): BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: Flow<ViewState> get() = _viewState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _viewState.emit(ViewState.Loading)
                Timber.d("load profile")
                // artificial delay
                delay(2000)

                when (val response = usersInteractor.loadProfile()) {
                    is NetworkResponse.Success -> {
                        _viewState.emit(ViewState.Data(response.body))
                    }
                    else -> {
                        // TODO
                        error("")
                    }
                }

            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            try {
                authInteractor.logout()
            } catch (error: Throwable) {
                Timber.e(error)
                _eventChannel.send(Event.LogoutError(error))
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val profile: User) : ViewState()
    }

    sealed class Event {
        data class LogoutError(val error: Throwable) : Event()
    }
}
