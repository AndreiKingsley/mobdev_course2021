package com.example.chinchopaapp.sign_in

import androidx.lifecycle.viewModelScope
import com.example.chinchopaapp.AuthRepository
import com.example.chinchopaapp.BaseViewModel
import kotlinx.coroutines.launch

class SignInViewModel: BaseViewModel() {
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            AuthRepository.signIn(email, password)
        }
    }

}