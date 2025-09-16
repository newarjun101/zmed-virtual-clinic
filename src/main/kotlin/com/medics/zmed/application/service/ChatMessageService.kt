package com.medics.zmed.application.service

import com.medics.zmed.application.mapper.response_model_mapper.toPaginationEmptySuccessModel
import com.medics.zmed.application.mapper.response_model_mapper.toPaginationSuccessModel
import com.medics.zmed.application.mapper.toChatMessageDao
import com.medics.zmed.application.mapper.toMessageResponseModel
import com.medics.zmed.common.exceptions.model.ResponsePaginationModel
import com.medics.zmed.component.JwtUtil
import com.medics.zmed.domain.model.request_model.MessageHistoryRequestModel
import com.medics.zmed.domain.model.request_model.MessageRequestModel
import com.medics.zmed.domain.model.response_model.ChatMessageResponseModel
import com.medics.zmed.domain.repository.ChatMessageRepository
import org.springframework.stereotype.Service


@Service
class ChatMessageService(
    private val messageRepository: ChatMessageRepository,
   private val jwtUtil: JwtUtil
) {

    fun getMessageByChatId(requestModel: MessageHistoryRequestModel? = null,token : String?= null): ResponsePaginationModel {

        if (token.isNullOrBlank()) {
            throw IllegalArgumentException("Invalid access token")
        }

        if(!jwtUtil.isTokenValid(token,false)) {
            throw IllegalArgumentException("Invalid or expire token")
        }

        if(jwtUtil.getUserId(token) != requestModel?.userId) {
            throw IllegalArgumentException("Invalid or expire token")
        }

        if (requestModel.chatId == null) {
            return emptyList<ChatMessageResponseModel>().toPaginationEmptySuccessModel()
        }
        val data = messageRepository.findAllChatId(chatId = requestModel.chatId, pageNumber = requestModel.pageNumber, pageSize = requestModel.pageSize)
        val messageResponseModel = data.content.map { it.toMessageResponseModel() }
        return data.toPaginationSuccessModel(messageResponseModel)
    }

    fun saveMessage(messageRequestModel: MessageRequestModel) {
        messageRepository.saveMessage(
            messageDao = messageRequestModel.toChatMessageDao()
        )

    }
}