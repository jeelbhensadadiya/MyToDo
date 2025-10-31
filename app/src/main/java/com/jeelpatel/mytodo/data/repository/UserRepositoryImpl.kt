package com.jeelpatel.mytodo.data.repository

import com.jeelpatel.mytodo.data.local.dao.UserDao
import com.jeelpatel.mytodo.domain.mapper.toDomain
import com.jeelpatel.mytodo.domain.mapper.toEntity
import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val userDao: UserDao) : UserRepository {

    override suspend fun registerUser(user: UserModel) = userDao.registerUser(user.toEntity())

    override fun loginUser(userEmail: String, userPassword: String): Flow<UserModel?> =
        userDao.loginUser(userEmail, userPassword).map { entity ->
            entity?.toDomain()
        }
}