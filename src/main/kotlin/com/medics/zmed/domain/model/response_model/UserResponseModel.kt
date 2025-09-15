package com.medics.zmed.domain.model.response_model

import com.medics.zmed.persistance.entity.UserDao

data class UserResponseModel(
    val id: Long,
    val name: String,
    val email: String,
    val accessToken: String?,
    val refreshToken: String?
) {
    companion object Companion {
        fun fromUserDao(userDao: UserDao) = UserResponseModel(
            id = userDao.id!!,
            name = userDao.name,
            email = userDao.email,
            accessToken = userDao.accessToken,
            refreshToken = userDao.refreshToken
        )
    }
}
