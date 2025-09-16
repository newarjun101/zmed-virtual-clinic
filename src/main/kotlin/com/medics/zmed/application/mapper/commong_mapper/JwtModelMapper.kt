package com.medics.zmed.application.mapper.commong_mapper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.medics.zmed.domain.model.common_model.JwtModel


fun String.toJwtModelMapper() : JwtModel{
    return jacksonObjectMapper().readValue<JwtModel>(this)
}

fun Any.writeValueAsString()  : String{
    return jacksonObjectMapper().writeValueAsString(this)
}