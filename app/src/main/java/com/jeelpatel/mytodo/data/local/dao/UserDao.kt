package com.jeelpatel.mytodo.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jeelpatel.mytodo.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert
    suspend fun registerUser(user: UserEntity)


    @Query("SELECT * FROM user_table WHERE uEmail = :userEmail AND uPassword = :userPassword LIMIT 1")
    fun loginUser(userEmail: String, userPassword: String): Flow<UserEntity?>

}