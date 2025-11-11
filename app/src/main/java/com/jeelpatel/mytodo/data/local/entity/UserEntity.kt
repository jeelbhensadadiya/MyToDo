package com.jeelpatel.mytodo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeelpatel.mytodo.utils.Config

@Entity(tableName = Config.USER_TABLE)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val uId: Int = 0,
    val uName: String,
    val uEmail: String,
    val uPassword: String,
    val uCreatedAt: Long = System.currentTimeMillis()
)