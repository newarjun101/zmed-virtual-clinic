package com.medics.zmed.domain.model.request_model
import com.fasterxml.jackson.annotation.JsonProperty
data class RefreshTokenRequestModel(

    @JsonProperty("refresh_token")
    val refreshToken : String? = null
)