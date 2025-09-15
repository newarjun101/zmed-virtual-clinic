package com.medics.zmed.common.exceptions.model

import com.fasterxml.jackson.annotation.JsonProperty

data class ResponseModel (
    val data : Any? = null,
    val message : String? = null,

    @JsonProperty("error_response")
    val errorResponse: ErrorResponse? = null
)


data class ResponsePaginationModel (
    val data : Any? = null,
    val pageNumber : Int? = null,
    val pageSize : Int? = null,
    val isFirst : Boolean? = null,
    val isLast : Boolean? = null,
)