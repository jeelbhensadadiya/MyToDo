package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoginUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    operator fun invoke(email: String, password: String): Flow<UserModel?> {
        return userRepository.loginUser(email, password)
    }
}