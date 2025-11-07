package com.jeelpatel.mytodo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jeelpatel.mytodo.data.local.entity.UserEntity

@Dao
interface UserDao {

    // insert new user data
    @Insert
    suspend fun registerUser(user: UserEntity)


    // fetch user data for login
    @Query("SELECT * FROM user_table WHERE uEmail = :userEmail AND uPassword = :userPassword LIMIT 1")
    fun loginUser(userEmail: String, userPassword: String): UserEntity?

}