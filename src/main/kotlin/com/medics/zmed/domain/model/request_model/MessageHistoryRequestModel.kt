package com.medics.zmed.domain.model.request_model

import com.fasterxml.jackson.annotation.JsonProperty

data class MessageHistoryRequestModel(
    @JsonProperty("chat_id")
    val chatId : String?=null,
    @JsonProperty("page_number")
    val pageNumber : Int = 0,
    @JsonProperty("page_size")
    val pageSize: Int = 10
)