package com.jeelpatel.mytodo.model

import com.jeelpatel.mytodo.model.local.dao.UserDao
import com.jeelpatel.mytodo.model.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity) = userDao.registerUser(user)

    fun loginUser(userEmail: String, userPassword: String): Flow<UserEntity?> =
        userDao.loginUser(userEmail, userPassword)
}