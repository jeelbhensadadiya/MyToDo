package com.jeelpatel.mytodo.domain.usecase.userUseCase

import com.jeelpatel.mytodo.domain.model.UserModel
import com.jeelpatel.mytodo.domain.repository.UserRepository
import javax.inject.Inject

class RegisterNewUserUseCase @Inject constructor(private val userRepository: UserRepository) {
    suspend operator fun invoke(
        userName: String,
        userEmail: String,
        userPassword: String,
        userRePassword: String
    ): Result<Unit> {

        // check name field
        if (userName.isEmpty()) {
            return Result.failure(Exception("Name Can not be Empty!!"))
        }

        // check email field
        if (userEmail.isEmpty()) {
            return Result.failure(Exception("Email Can not be Empty!!"))
        }

        // check password field
        if (userPassword.isEmpty()) {
            return Result.failure(Exception("Password Can not be Empty!!"))
        }

        // check re-password field
        if (userRePassword.isEmpty()) {
            return Result.failure(Exception("Re Password Can not be Empty!!"))
        }

        // validate passwords
        if (userPassword != userRePassword) {
            return Result.failure(Exception("Password and RePassword Mismatch "))
        }

        // store user data into UserModel
        val userModel = UserModel(
            uName = userName,
            uEmail = userEmail,
            uPassword = userPassword
        )

        // return data or errors
        return try {
            userRepository.registerUser(userModel)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}