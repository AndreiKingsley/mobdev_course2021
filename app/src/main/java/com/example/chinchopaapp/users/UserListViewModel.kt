package com.example.chinchopaapp.users

import androidx.lifecycle.viewModelScope
import com.example.chinchopaapp.BaseViewModel
import com.example.chinchopaapp.entity.User
import com.example.chinchopaapp.interactor.UsersInteractor
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val usersInteractor: UsersInteractor
) : BaseViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.Loading)
    val viewState: Flow<ViewState> get() = _viewState.asStateFlow()


    private suspend fun loadUsers(): List<User> {
        when (val response = usersInteractor.loadUsers()) {
            is NetworkResponse.Success -> {
                return response.body
            }
            else -> {
                // TODO
                error("")
            }
        }
    }

    fun searchUsers(key: String? = null, bySubstring: Boolean = true) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                _viewState.emit(ViewState.Loading)
                Timber.d("load users()")
                // artificial delay
                delay(2000)

                val response = loadUsers()
                val result = if (key == null){
                    response
                } else if (bySubstring) {
                    response.filter { it.userName.contains(key) }
                } else {
                    response.filter { it.userName == key }
                }
                _viewState.emit(ViewState.Data(result))

            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val userList: List<User>) : ViewState()
    }


}
