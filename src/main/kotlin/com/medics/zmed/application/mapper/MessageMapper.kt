package com.medics.zmed.application.mapper

import com.medics.zmed.domain.model.request_model.MessageRequestModel
import com.medics.zmed.domain.model.response_model.ChatMessageResponseModel
import com.medics.zmed.persistance.entity.ChatMessageDao
import java.time.LocalDateTime


fun ChatMessageDao.toMessageResponseModel(): ChatMessageResponseModel {
    return ChatMessageResponseModel(
        id = id,
        chatId = chatId,
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        createdAt = createdAt
    )
}
fun MessageRequestModel.toChatMessageDao() : ChatMessageDao {
    
    return ChatMessageDao(
        chatId = this.chatId?:"",
        senderId = senderId,
        receiverId = receiverId,
        content = content,
        createdAt = LocalDateTime.now()
    )
    
}