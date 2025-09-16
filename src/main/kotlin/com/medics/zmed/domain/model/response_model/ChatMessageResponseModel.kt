package com.medics.zmed.domain.model.response_model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ChatMessageResponseModel(
    val id: Long?=null,
    @JsonProperty("chat_id")
    val chatId: String,
    @JsonProperty("sender_id")
    val senderId: Long,
    @JsonProperty("receiver_id")
    val receiverId: Long,
    val content: String,
    @JsonProperty("created_at")
    val createdAt: LocalDateTime
)