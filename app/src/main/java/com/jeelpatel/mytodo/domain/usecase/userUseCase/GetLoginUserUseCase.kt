package com.jeelpatel.mytodo.domain.usecase.userUseCase

import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.repository.UserRepository
import javax.inject.Inject

class GetLoginUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Result<UserModel> {


        // check email field
        if (email.isEmpty()) {
            return Result.failure(Exception("Email cannot be empty"))
        }

        // check password field
        if (password.isEmpty()) {
            return Result.failure(Exception("Password cannot be empty"))
        }

        // return data or errors
        return try {

            val user = userRepository.loginUser(email, password)
            if (user != null) {

                // return user data
                Result.success(user)
            } else {

                // return if user not found or invalid credentials
                Result.failure(Exception("Invalid email or password"))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}