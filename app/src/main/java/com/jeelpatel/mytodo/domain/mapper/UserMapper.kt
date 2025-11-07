package com.jeelpatel.mytodo.domain.mapper

import com.jeelpatel.mytodo.data.local.entity.UserEntity
import com.jeelpatel.mytodo.domain.model.UserModel

// map UserEntity to UserModel
fun UserEntity.toDomain(): UserModel {
    return UserModel(
        uId = uId,
        uName = uName,
        uEmail = uEmail,
        uPassword = uPassword
    )
}


// map UserModel to UserEntity
fun UserModel.toEntity(): UserEntity {
    return UserEntity(
        uId = 0,
        uName = uName,
        uEmail = uEmail,
        uPassword = uPassword,
        uCreatedAt = System.currentTimeMillis()
    )
}