package com.medics.zmed.common.exceptions.model

data class ErrorResponse(
    val message: String,
    val status: Int,
   val description : String
)