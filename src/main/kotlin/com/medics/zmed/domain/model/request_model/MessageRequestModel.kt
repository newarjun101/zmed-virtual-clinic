package com.medics.zmed.domain.model.request_model
import com.fasterxml.jackson.annotation.JsonProperty
import com.medics.zmed.persistance.entity.MessageStatus
import java.time.LocalDateTime

data class MessageRequestModel(


    @JsonProperty("sender_id")
    val senderId: Long,
    @JsonProperty("receiver_id")
    val receiverId: Long,
    val content: String,
    @JsonProperty("chat_id")
    val chatId : String ?=null,
    @JsonProperty("message_status")
    val messageStatus :  MessageRequestStatus? = MessageRequestStatus.SENT
)


enum class MessageRequestStatus {
    SENT, DELIVERED, SEEN
}