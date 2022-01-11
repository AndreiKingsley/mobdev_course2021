package com.example.chinchopaapp.repository

import com.example.chinchopaapp.data.network.Api
import com.example.chinchopaapp.data.network.response.VerificationTokenResponse
import com.example.chinchopaapp.data.network.response.error.SendRegistrationVerificationCodeErrorResponse
import com.example.chinchopaapp.data.network.response.error.VerifyRegistrationCodeErrorResponse
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject
import dagger.Lazy
import javax.inject.Singleton

@Singleton
class VerificationCodeRepository @Inject constructor(
    private val apiLazy: Lazy<Api>,
) {
    private val api by lazy { apiLazy.get() }

    suspend fun sendVerificationCodeToEmail(
        email: String
    ): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse> {
        return api.sendRegistrationVerificationCode(email)
    }

    suspend fun verifyEmailRegistrationCode(
        code: String,
        email: String
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse> {
        return api.verifyRegistrationCode(code, email)
    }
}