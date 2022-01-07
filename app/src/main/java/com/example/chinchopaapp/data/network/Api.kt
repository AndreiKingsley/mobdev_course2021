package com.example.chinchopaapp.data.network

import com.example.chinchopaapp.data.network.request.*
import com.example.chinchopaapp.data.network.response.*
import com.example.chinchopaapp.data.network.response.error.*
import com.example.chinchopaapp.entity.AuthTokens
import com.haroldadmin.cnradapter.NetworkResponse
import com.example.chinchopaapp.data.network.request.CreateProfileRequest
import com.example.chinchopaapp.entity.User
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.*

interface Api {

    @GET("users?per_page=10")
    suspend fun getUsers(): GetUsersResponse

    @POST("auth/sign-in-email")
    suspend fun signInWithEmail(
        @Body request: SignInWithEmailRequest
    ): NetworkResponse<AuthTokens, SignInWithEmailErrorResponse>

    @POST("auth/refresh-tokens")
    suspend fun refreshAuthTokens(
        @Body request: RefreshAuthTokensRequest
    ): NetworkResponse<AuthTokens, RefreshAuthTokensErrorResponse>

    @POST("registration/verification-code/send")
    suspend fun sendRegistrationVerificationCode(
        @Query("email") email: String,
    ): NetworkResponse<Unit, SendRegistrationVerificationCodeErrorResponse>

    @POST("registration/verification-code/verify")
    suspend fun verifyRegistrationCode(
        @Query("code") code: String,
        @Query("email") email: String?,
        @Query("phone_number") phoneNumber: String?
    ): NetworkResponse<VerificationTokenResponse, VerifyRegistrationCodeErrorResponse>

    @PUT("registration/create-profile")
    suspend fun createProfile(
        @Body request: CreateProfileRequest
    ): NetworkResponse<AuthTokens, CreateProfileErrorResponse>
}

@JsonClass(generateAdapter = true)
data class GetUsersResponse(
    @Json(name = "data") val data: List<User>
)