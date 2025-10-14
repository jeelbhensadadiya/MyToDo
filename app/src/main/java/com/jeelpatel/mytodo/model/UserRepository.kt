package com.jeelpatel.mytodo.model

import com.jeelpatel.mytodo.model.local.dao.UserDao
import com.jeelpatel.mytodo.model.local.entity.UserEntity

class UserRepository(private val userDao: UserDao) {

    suspend fun registerUser(user: UserEntity) = userDao.registerUser(user)

    suspend fun loginUser(userEmail: String, userPassword: String) =
        userDao.loginUser(userEmail, userPassword)
}