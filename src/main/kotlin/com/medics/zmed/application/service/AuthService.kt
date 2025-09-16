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
import com.medics.zmed.domain.model.common_model.JwtModel
import com.medics.zmed.domain.model.request_model.RefreshTokenRequestModel
import com.medics.zmed.domain.model.response_model.RefreshTokenResponseModel
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
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

        val jwtModel = JwtModel(email = userDao.email, userId = userDao.id!!)
        val refreshToken = jwtUtil.generateRefreshToken(jwtModel)

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

        val userDao = UserDao(
            name = registerRequestModel.name,
            email = registerRequestModel.email,
            password = hashedPassword,
        )
        val userSignUpDao = authRepository.onSignUp(userDao)



        if (userSignUpDao.id == null) {
            throw IllegalArgumentException("User id getting null")
        }

        val jwtModel = JwtModel(email = userSignUpDao.email, userId = userSignUpDao.id)
        val newAccessToken = jwtUtil.generateAccessToken(jwtModel)
        val newRefreshToken = jwtUtil.generateRefreshToken(jwtModel)
        val update = authRepository.updateAndRefreshToken(userSignUpDao.id, newRefreshToken, newAccessToken)
        if (update == 0 || update == null) {
            throw UserNotFoundException("User with email not found")
        }
        val userResult = authRepository.findByEmail(userSignUpDao.email)
        print("arjun user result ==> ${userResult}")

        if (userResult == null) {
            throw IllegalArgumentException("Something gone wrong")
        }

        print("arjun access token ${userResult.accessToken}")
        return UserResponseModel.fromUserDao(
            userResult.copy(
                accessToken = newAccessToken, refreshToken = newRefreshToken
            )
        )

    }

    fun getUserById(id: Long, token : String?=null): UserResponseModel? {

        if (token.isNullOrBlank()) {
            throw UnauthorizedException("Token cannot be null or empty")
        }

        if(!jwtUtil.isTokenValid(token,false)) {
            throw UnauthorizedException("Invalid or token expire")
        }
        val jwtModel = jwtUtil.getJwtModel(token)

        print("arjun jtw ==> jwt id ${jwtModel.userId} || ${id} <===")
        if(jwtModel.userId != id ) {
            throw UnauthorizedException("Unmatch user")
        }
        val userDao = authRepository.findById(id)

        if(userDao?.email != jwtModel.email) {
            throw UnauthorizedException("Unmatch email")
        }
        return UserResponseModel.fromUserDao(userDao)

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

        val jwtModel = JwtModel(email = user.email, userId = user.id!!)

        val newAccessToken = jwtUtil.generateAccessToken(jwtModel)
        val newRefreshToken = jwtUtil.generateRefreshToken(jwtModel)

        val update = authRepository.updateAndRefreshToken(user.id, newRefreshToken, newAccessToken)
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