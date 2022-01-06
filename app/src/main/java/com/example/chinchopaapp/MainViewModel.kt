package com.example.chinchopaapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chinchopaapp.interactor.AuthInteractor
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authInteractor: AuthInteractor
) : BaseViewModel() {

    suspend fun isAuthorizedFlow(): Flow<Boolean> = authInteractor.isAuthorized()
}