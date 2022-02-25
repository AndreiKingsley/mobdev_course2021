package com.example.chinchopaapp.interactor

import com.example.chinchopaapp.data.network.response.VerificationTokenResponse
import com.example.chinchopaapp.data.network.response.error.CreateProfileErrorResponse
import com.example.chinchopaapp.data.network.response.error.SendRegistrationVerificationCodeErrorResponse
import com.example.chinchopaapp.data.network.response.error.VerifyRegistrationCodeErrorResponse
import com.example.chinchopaapp.entity.AuthTokens
import com.example.chinchopaapp.repository.AuthRepository
import com.example.chinchopaapp.repository.VerificationCodeRepository
import com.haroldadmin.cnradapter.NetworkResponse
import timber.log.Timber
import javax.inject.Inject

class SignUpInteractor @Inject constructor(
    private val authRepository: AuthRepository,
    private val verificationCodeRepository: VerificationCodeRepository
) {

    var cachedEmail: String? = null

    suspend fun signUpWithEmailAndInfo(
        email: String,
        firstName: String,
        lastName: String,
        userName: String,
        password: String,
        code: String
    ): NetworkResponse<AuthTokens, CreateProfileErrorResponse> {
        cachedEmail = email
        val response = authRepository.generateAuthTokensByEmailAndPersonalInfo(
            email,
            code,
            firstName,
            lastName,
            userName,
            password
        )
        when (response) {
            is NetworkResponse.Success -> {
                authRepository.saveAuthTokens(response.body)
            }
            is NetworkResponse.Error -> {
                Timber.e(response.error)
            }
            else -> {
                TODO()
            }
        }
        return response
    }

    suspend fun sendVerificationCode(): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse> {
        return cachedEmail?.let {
            verificationCodeRepository.sendVerificationCodeToEmail(it)
        } ?: error("null email")
    }

    suspend fun verifyEmail(
        code: String
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse> {
        return cachedEmail?.let {
            verificationCodeRepository.verifyEmailRegistrationCode(code, it)
        } ?: error("null email")
    }
}