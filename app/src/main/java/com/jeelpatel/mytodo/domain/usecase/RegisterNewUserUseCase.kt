package com.jeelpatel.mytodo.domain.usecase

import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.repository.UserRepository
import javax.inject.Inject

class RegisterNewUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(user: UserModel) {
        userRepository.registerUser(user)
    }
}