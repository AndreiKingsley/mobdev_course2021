package com.example.chinchopaapp

import com.example.chinchopaapp.data.network.Api
import com.example.chinchopaapp.data.network.request.CreateProfileRequest
import com.example.chinchopaapp.data.network.request.RefreshAuthTokensRequest
import com.example.chinchopaapp.data.network.request.SignInWithEmailRequest
import com.example.chinchopaapp.data.network.response.error.CreateProfileErrorResponse
import com.example.chinchopaapp.data.network.response.error.RefreshAuthTokensErrorResponse
import com.example.chinchopaapp.data.network.response.error.SignInWithEmailErrorResponse
import com.example.chinchopaapp.data.persistent.LocalKeyValueStorage
import com.example.chinchopaapp.di.AppCoroutineScope
import com.example.chinchopaapp.di.IoCoroutineDispatcher
import com.example.chinchopaapp.entity.AuthTokens
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.haroldadmin.cnradapter.NetworkResponse
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.Timber
import javax.inject.Inject
import kotlinx.coroutines.*
import dagger.Lazy
import kotlinx.coroutines.flow.*
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val apiLazy: Lazy<Api>,
    private val localKeyValueStorage: LocalKeyValueStorage,
    @AppCoroutineScope externalCoroutineScope: CoroutineScope,
    @IoCoroutineDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private val api by lazy { apiLazy.get() }

    private val authTokensFlow: Deferred<MutableStateFlow<AuthTokens?>> =
        externalCoroutineScope.async(context = ioDispatcher, start = CoroutineStart.LAZY) {
            Timber.d("Initializing auth tokens flow.")
            MutableStateFlow(
                localKeyValueStorage.authTokens
            )
        }

    suspend fun getAuthTokensFlow(): StateFlow<AuthTokens?> {
        return authTokensFlow.await().asStateFlow()
    }

    /**
     * @param authTokens active auth tokens which must be used for signing all requests
     */
    suspend fun saveAuthTokens(authTokens: AuthTokens?) {
        withContext(ioDispatcher) {
            Timber.d("Persist auth tokens $authTokens.")
            localKeyValueStorage.authTokens = authTokens
        }
        Timber.d("Emit auth tokens $authTokens.")
        authTokensFlow.await().emit(authTokens)
    }

    /**
     * @return whether active access tokens are authorized or not
     */
    suspend fun isAuthorizedFlow(): Flow<Boolean> {
        return authTokensFlow
            .await()
            .asStateFlow()
            .map { it != null }
    }

    suspend fun generateAuthTokensByEmail(
        email: String,
        password: String
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        return api.signInWithEmail(SignInWithEmailRequest(email, password))
    }

    /**
     * Creates a user account in the system as a side effect.
     * @return access tokens with higher permissions for the new registered user
     */
    suspend fun generateAuthTokensByEmailAndPersonalInfo(
        email: String,
        verificationToken: String,
        firstName: String,
        lastName: String,
        //username: String,
        password: String
    ): NetworkResponse<AuthTokens, CreateProfileErrorResponse> {
        return api.createProfile(
            CreateProfileRequest(
                verificationToken,
                firstName,
                lastName,
               // username,
                email,
                password
            )
        )
    }

    suspend fun generateRefreshedAuthTokens(refreshToken: String): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse> {
        return api.refreshAuthTokens(RefreshAuthTokensRequest(refreshToken))
    }
}