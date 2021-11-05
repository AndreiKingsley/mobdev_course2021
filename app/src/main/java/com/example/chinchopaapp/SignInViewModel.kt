package com.example.chinchopaapp

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignInViewModel: BaseViewModel() {
    fun signIn() {
        viewModelScope.launch {
            AuthRepository.signIn()
        }
    }

}