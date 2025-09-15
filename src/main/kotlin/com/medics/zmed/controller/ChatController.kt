package com.medics.zmed.controller

import com.medics.zmed.application.mapper.response_model_mapper.toSuccessModel
import com.medics.zmed.application.service.ChatMessageService
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.common.exceptions.model.ResponsePaginationModel
import com.medics.zmed.domain.model.request_model.MessageHistoryRequestModel
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
    @RequestMapping("/api/v1/chat")
    class ChatController(
        private val chatMessageService: ChatMessageService
    ) {
        @PostMapping("/history")
        fun getChatHistory(
            @RequestBody body : MessageHistoryRequestModel?=null,
        ): ResponseEntity<ResponseModel> {
            val responseModel = chatMessageService.getMessageByChatId(body)
            return ResponseEntity.status(HttpStatus.OK).body(responseModel.toSuccessModel())
        }
    }

