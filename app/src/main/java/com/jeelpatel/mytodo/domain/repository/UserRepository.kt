package com.jeelpatel.mytodo.domain.repository

import com.jeelpatel.mytodo.domain.model.UserModel

interface UserRepository {


    suspend fun registerUser(user: UserModel)


    suspend fun loginUser(userEmail: String, userPassword: String): UserModel?
}