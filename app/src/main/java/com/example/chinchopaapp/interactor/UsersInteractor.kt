package com.example.chinchopaapp.interactor

import com.example.chinchopaapp.repository.UsersRepository
import com.example.chinchopaapp.entity.User
import com.haroldadmin.cnradapter.NetworkResponse
import javax.inject.Inject

class UsersInteractor @Inject constructor(
    private val usersRepository: UsersRepository
) {
    suspend fun loadUsers(): NetworkResponse<List<User>, Unit> =
        usersRepository.getUsers()
}
