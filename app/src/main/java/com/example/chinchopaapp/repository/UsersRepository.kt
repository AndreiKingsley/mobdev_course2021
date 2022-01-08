package com.example.chinchopaapp.repository

import com.example.chinchopaapp.data.network.Api
import com.example.chinchopaapp.entity.User
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.Lazy
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsersRepository @Inject constructor(
    private val apiLazy: Lazy<Api>
) {

    private val api by lazy { apiLazy.get() }

    suspend fun getUsers(): NetworkResponse<List<User>, Unit> =
        api.getUsers()
} 