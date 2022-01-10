package com.example.chinchopaapp.data.network

import com.example.chinchopaapp.data.network.request.CreateProfileRequest
import com.example.chinchopaapp.data.network.request.RefreshAuthTokensRequest
import com.example.chinchopaapp.data.network.request.SignInWithEmailRequest
import com.example.chinchopaapp.data.network.response.VerificationTokenResponse
import com.example.chinchopaapp.data.network.response.error.*
import com.example.chinchopaapp.entity.AuthTokens
import com.example.chinchopaapp.entity.User
import com.haroldadmin.cnradapter.NetworkResponse
import timber.log.Timber

class MockApi : Api {
    private val dummyUser = User(
        1,
        "Pavlov",
        "Vanyek",
        "softly_p",
        "https://startgames.org/wp-content/uploads/2020/12/%D0%91%D0%BE%D0%BB%D1%8C%D1%88%D0%BE%D0%B9-%D1%88%D0%BB%D0%B5%D0%BF%D0%B0-%D0%A4%D0%BB%D0%BE%D0%BF%D0%BF%D0%B0-%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9-%D0%BA%D0%BE%D1%82-5.jpg",
        "Shleppa`s Fans",
        "21"
    )

    override suspend fun getProfile(): NetworkResponse<User, Unit> {
        return NetworkResponse.Success(
            dummyUser,
            code = 200
        )
    }

    override suspend fun getUsers(): NetworkResponse<List<User>, Unit> {
        val response = listOf(
            dummyUser,
            dummyUser,
            dummyUser
        )
        Timber.d("Users mock ${response.size}}")
        return NetworkResponse.Success(
            response,
            code = 200
        )
    }


    override suspend fun signInWithEmail(request: SignInWithEmailRequest): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse> {
        Timber.d("RETURN SUCCESS")
        return NetworkResponse.Success(
            AuthTokens(
                accessToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2MzgsImV4cCI6MTY0MDg3MTc3MX0.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI",
                refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJsb2dnZWRJbkFzIjoiYWRtaW4iLCJpYXQiOjE0MjI3Nzk2MzgsImV4cCI6MTY0MDg3MTc3MX0.gzSraSYS8EXBxLN_oWnFSRgCzcmJmMjLiuyu5CSpyHI",
                accessTokenExpiration = 1640871771000,
                refreshTokenExpiration = 1640871771000,
            ),
            code = 200
        )
    }

    override suspend fun refreshAuthTokens(request: RefreshAuthTokensRequest): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendRegistrationVerificationCode(email: String): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun verifyRegistrationCode(
        code: String,
        email: String?,
        phoneNumber: String?
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun createProfile(request: CreateProfileRequest): NetworkResponse<AuthTokens, CreateProfileErrorResponse> {
        TODO("Not yet implemented")
    }
}