package com.jeelpatel.mytodo.domain.repository

import com.jeelpatel.mytodo.domain.model.UserModel
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun registerUser(user: UserModel)

    fun loginUser(userEmail: String, userPassword: String): Flow<UserModel?>
}