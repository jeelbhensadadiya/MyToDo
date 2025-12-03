package com.jeelpatel.mytodo.data.repository

import com.jeelpatel.mytodo.data.local.dao.UserDao
import com.jeelpatel.mytodo.domain.mapper.toDomain
import com.jeelpatel.mytodo.domain.mapper.toEntity
import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {


    // create new user
    override suspend fun registerUser(user: UserModel): Result<String> {
        val existingUser = userDao.getUserByEmail(user.uEmail)

        return if (existingUser != null) {
            Result.failure(Exception("409"))
        } else {
            userDao.registerUser(user.toEntity())
            Result.success("User registered successfully")
        }
    }


    // login or fetch loggedIn user's data
    override suspend fun loginUser(userEmail: String, userPassword: String): UserModel? =
        userDao.loginUser(userEmail, userPassword)?.toDomain()
}