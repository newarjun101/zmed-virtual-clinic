package com.medics.zmed.domain.model.common_model

import jakarta.validation.constraints.Email

data class JwtModel(
    val email: String,
    val userId : Long
)