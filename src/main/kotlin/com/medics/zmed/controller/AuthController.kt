package com.medics.zmed.controller

import com.medics.zmed.application.mapper.response_model_mapper.toSuccessModel
import com.medics.zmed.application.service.AuthService
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.domain.model.request_model.LoginRequestModel
import com.medics.zmed.domain.model.request_model.RefreshTokenRequestModel
import com.medics.zmed.domain.model.request_model.RegisterRequestModel
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("api/v1/auth")
@Tag(name = "Auth Related API", description = "Endpoints to manage Auth")
class AuthController(
    private val authService: AuthService
) {


    @PostMapping("/process/login")
    fun login(@RequestBody body: LoginRequestModel? = null): ResponseEntity<ResponseModel> {
        if (body == null) {
            throw IllegalArgumentException("Request body is empty")
        }
        val userResponse = authService.login(body)
        return ResponseEntity.status(HttpStatus.OK).body(userResponse?.toSuccessModel()) // 201 Created

    }

    @PostMapping("/process/create-account")
    fun createAccount(@RequestBody body: RegisterRequestModel? = null): ResponseEntity<ResponseModel> {
        if (body == null) {
            throw IllegalArgumentException("Request body is empty")
        }
        val userResponse = authService.registerUser(body)

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse.toSuccessModel()) // 201 Created

    }

    @GetMapping("/get-user-profile")
    fun findById(@RequestParam id: Long? = null): ResponseEntity<ResponseModel> {
        if (id == null) {
            throw IllegalArgumentException("Invalid request")
        }
        val userResponse = authService.getUserById(id)
        if (userResponse == null) {
            throw IllegalArgumentException("Invalid id")
        }
        return ResponseEntity.status(HttpStatus.OK).body(userResponse.toSuccessModel())
    }


    @PostMapping("/process/refresh-token")
    fun requestNewAccessToken(@RequestBody body: RefreshTokenRequestModel? = null): ResponseEntity<ResponseModel> {
        if (body == null) {
            throw IllegalArgumentException("Request body is empty")
        }

        val tokenModel = authService.refreshToken(
            refreshTokenRequestModel = body
        )

        return ResponseEntity.status(HttpStatus.OK).body(tokenModel?.toSuccessModel())

    }
}