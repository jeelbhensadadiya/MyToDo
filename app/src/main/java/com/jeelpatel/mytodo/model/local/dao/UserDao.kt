package com.jeelpatel.mytodo.model.local.dao

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query
import com.jeelpatel.mytodo.model.local.entity.UserEntity

@Dao
interface UserDao {

    @Insert
    suspend fun registerUser(user: UserEntity)

    @Query("SELECT * FROM user_table WHERE uEmail = :userEmail AND uPassword = :userPassword LIMIT 1")
    suspend fun loginUser(userEmail: String, userPassword: String): UserEntity?

}