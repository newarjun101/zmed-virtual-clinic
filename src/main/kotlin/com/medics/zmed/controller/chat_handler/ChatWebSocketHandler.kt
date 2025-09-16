package com.medics.zmed.controller.chat_handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.medics.zmed.application.mapper.commong_mapper.writeValueAsString
import com.medics.zmed.application.service.ChatMessageService
import com.medics.zmed.common.exceptions.customExceptions.UnauthorizedException
import com.medics.zmed.common.exceptions.model.ErrorResponse
import com.medics.zmed.common.exceptions.model.ResponseModel
import com.medics.zmed.common.util.ChatUtil
import com.medics.zmed.component.ConnectionManager
import com.medics.zmed.component.JwtUtil
import com.medics.zmed.domain.model.request_model.MessageRequestModel
import com.medics.zmed.domain.repository.AuthRepository
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ChatWebSocketHandler(
    private val connectionManager: ConnectionManager,
    private val chatMessageService: ChatMessageService,
    private val jwtUtil: JwtUtil,
    private val authRepository: AuthRepository,
) : TextWebSocketHandler() {

    private val objectMapper = jacksonObjectMapper()

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val headers = session.handshakeHeaders
        val authHeader = headers["Authorization"]?.firstOrNull()

        if (authHeader.isNullOrBlank() || !authHeader.startsWith("Bearer ")) {
            sendError(session, ResponseModel(
                errorResponse = ErrorResponse(
                    message = "Missing or invalid Authorization header",
                    status = 401,
                    description = "You must provide a valid Authorization token"
                )
            ))
            session.close(CloseStatus.NOT_ACCEPTABLE)
            return
        }

        val token = authHeader.removePrefix("Bearer ").trim()
        if (!jwtUtil.isTokenValid(token, isRefreshToken = false)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or expired token"))
            return
        }

        val userId = jwtUtil.getUserId(token)
        val user = authRepository.findById(userId)
            ?: throw IllegalStateException("User not found for token")

        session.attributes["userId"] = user.id!!
        session.attributes["email"] = user.email

        connectionManager.addUser(user.id, session)
        println(" User ${user.id} connected via WebSocket")
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        try {
            var messageRequest: MessageRequestModel = objectMapper.readValue(message.payload)

            val currentUserId = session.attributes["userId"] as? Long
                ?: throw UnauthorizedException("Session has no userId")

            if (messageRequest.senderId != currentUserId) {
                throw UnauthorizedException("Sender ID does not match session user")
            }

            // Generate chatId if missing
            if (messageRequest.chatId.isNullOrBlank()) {
                val chatId = ChatUtil.generateChatId(messageRequest.senderId, messageRequest.receiverId)
                messageRequest = messageRequest.copy(chatId = chatId)
            }

            chatMessageService.saveMessage(messageRequest)

            //  Get receiver session from connectionManager (not local map!)
            val receiverSession = connectionManager.getSession(messageRequest.receiverId)
            if (receiverSession != null && receiverSession.isOpen) {
                receiverSession.sendMessage(TextMessage(objectMapper.writeValueAsString(messageRequest)))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (session.isOpen) {
                session.sendMessage(TextMessage("""{"error":"${e.message}"}"""))
            }
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userId = session.attributes["userId"] as? Long
        if (userId != null) {
            connectionManager.removeUser(userId)
            println(" User $userId disconnected")
        }
    }

    private fun sendError(session: WebSocketSession, responseModel: ResponseModel) {
        if (session.isOpen) {
            session.sendMessage(TextMessage(responseModel.writeValueAsString()))
        }
    }
}
