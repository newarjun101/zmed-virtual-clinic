package com.medics.zmed.domain.model.request_model

data class LoginRequestModel(
    val email : String? = null,
    val password : String
)