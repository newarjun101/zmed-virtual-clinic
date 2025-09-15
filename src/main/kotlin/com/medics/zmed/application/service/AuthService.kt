package com.medics.zmed.application.service

import com.medics.zmed.common.exceptions.customExceptions.UnauthorizedException
import com.medics.zmed.component.JwtUtil
import com.medics.zmed.domain.model.request_model.LoginRequestModel
import com.medics.zmed.domain.model.request_model.RegisterRequestModel
import com.medics.zmed.domain.model.response_model.UserResponseModel
import com.medics.zmed.domain.repository.AuthRepository
import com.medics.zmed.persistance.entity.UserDao
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import com.medics.zmed.common.exceptions.customExceptions.UserNotFoundException
import com.medics.zmed.domain.model.request_model.RefreshTokenRequestModel
import com.medics.zmed.domain.model.response_model.RefreshTokenResponseModel
import kotlin.NullPointerException

@Service
class AuthService(
    private val authRepository: AuthRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    fun login(loginRequestModel: LoginRequestModel): UserResponseModel? {

        if (loginRequestModel.email.isNullOrBlank()) {
            throw IllegalArgumentException("Email is empty or null")
        }

        if (loginRequestModel.password.trim().length < 5) {
            throw IllegalArgumentException("Password is too short")
        }

        val userDao = authRepository.findByEmail(loginRequestModel.email)

        if (userDao?.email == null) {
            throw UserNotFoundException("There is not user with email ${loginRequestModel.email}")
        }

        if (!passwordEncoder.matches(loginRequestModel.password, userDao.password)) {
            throw IllegalArgumentException("Wrong password")
        }

        val accessToken = jwtUtil.generateAccessToken(loginRequestModel.email)
        val refreshToken = jwtUtil.generateRefreshToken(loginRequestModel.email)

        val tokenRequest = refreshToken(
            refreshTokenRequestModel = RefreshTokenRequestModel(refreshToken)
        )

        if (tokenRequest?.refreshToken.isNullOrBlank() || tokenRequest.accessToken.isBlank()) {
            throw IllegalArgumentException("Something gone wrong in generation token")
        }

        return UserResponseModel.fromUserDao(
            userDao.copy(
                accessToken = tokenRequest.accessToken,
               refreshToken = tokenRequest.refreshToken
            )
        )
    }


    fun registerUser(registerRequestModel: RegisterRequestModel): UserResponseModel {

        if (registerRequestModel.password.trim().length < 6) {
            throw IllegalArgumentException("Password is too short")
        }
        if (authRepository.findByEmail(registerRequestModel.email) != null) {

            throw IllegalArgumentException("Email is already registered")
        }

        val hashedPassword = passwordEncoder.encode(registerRequestModel.password)

        if (hashedPassword.isNullOrBlank()) {
            throw IllegalArgumentException("Password encryption error")
        }
        val accessToken = jwtUtil.generateAccessToken(registerRequestModel.email)
        val refreshToken = jwtUtil.generateRefreshToken(registerRequestModel.email)

        val userDao = UserDao(
            name = registerRequestModel.name,
            email = registerRequestModel.email,
            password = hashedPassword,
            accessToken = accessToken,
            refreshToken = refreshToken
        )
        val data = authRepository.onSignUp(userDao)
        return UserResponseModel.fromUserDao(data)

    }

    fun getUserById(id: Long): UserResponseModel? {
        val userDao = authRepository.findById(id)

        return if (userDao != null) {
            UserResponseModel.fromUserDao(userDao)
        } else {
            null
        }

    }


    fun refreshToken(refreshTokenRequestModel: RefreshTokenRequestModel): RefreshTokenResponseModel? {

        if (refreshTokenRequestModel.refreshToken.isNullOrBlank()) {
            throw NullPointerException("Refresh token is null or empty")
        }
        if (!jwtUtil.isTokenValid(refreshTokenRequestModel.refreshToken, isRefreshToken = true)) {
            throw UnauthorizedException("Invalid token or expire")
        }
        val email = jwtUtil.getEmailFromToken(refreshTokenRequestModel.refreshToken)

        val user = authRepository.findByEmail(email)
            ?: throw UserNotFoundException("User not found for refresh token")
        val newAccessToken = jwtUtil.generateAccessToken(user.email)
        val newRefreshToken = jwtUtil.generateRefreshToken(user.email)

        val update = authRepository.refreshToken(email, newRefreshToken, newAccessToken)
        if (update == 0 || update == null) {
            throw UserNotFoundException("User with email $email not found")
        }

        val userDao = authRepository.findByEmail(email)

        if (userDao?.refreshToken.isNullOrBlank() || userDao.accessToken.isNullOrBlank()) {
            throw UserNotFoundException("Unexpected error happen in database for access token")

        }

        return RefreshTokenResponseModel(accessToken = userDao.accessToken, userDao.refreshToken)

    }


}