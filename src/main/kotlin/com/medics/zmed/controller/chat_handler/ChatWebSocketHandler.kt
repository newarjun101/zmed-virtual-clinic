package com.medics.zmed.controller.chat_handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.medics.zmed.application.service.ChatMessageService
import com.medics.zmed.common.exceptions.customExceptions.UnauthorizedException
import com.medics.zmed.common.util.ChatUtil
import com.medics.zmed.component.ConnectionManager
import com.medics.zmed.component.JwtUtil
import com.medics.zmed.domain.model.request_model.MessageRequestModel
import com.medics.zmed.domain.repository.AuthRepository
import com.medics.zmed.persistance.entity.ChatMessageDao
import com.medics.zmed.persistance.entity.MessageStatus
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.time.LocalDateTime

@Component
class ChatWebSocketHandler(
    private val connectionManager: ConnectionManager,
    private val chatMessageService: ChatMessageService,
    private val jwtUtil: JwtUtil,
    private val authRepository: AuthRepository,
) : TextWebSocketHandler() {

    private val sessions = mutableMapOf<Long, WebSocketSession>()  // userId → session
    private val objectMapper = jacksonObjectMapper()
    override fun afterConnectionEstablished(session: WebSocketSession) {


        val headers = session.handshakeHeaders
        val authHeader = headers["Authorization"]?.firstOrNull() ?: run {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Missing Authorization header"))
            return
        }

        if (!authHeader.startsWith("Bearer ")) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid Authorization format"))
            return
        }

        val token = authHeader.removePrefix("Bearer ").trim()
        if (!jwtUtil.isTokenValid(token, isRefreshToken = false)) {
            session.close(CloseStatus.NOT_ACCEPTABLE.withReason("Invalid or expired token"))
            return
        }

        val email = jwtUtil.getEmailFromToken(token)
        val user = authRepository.findByEmail(email)
            ?: throw IllegalStateException("User not found for token")

        session.attributes["userId"] = user.id as Long
        session.attributes["email"] = email

        connectionManager.addUser(user.id, session)
        println("User ${user.id} connected via WebSocket")
    }

    override fun handleTextMessage(
        session: WebSocketSession,
        message: TextMessage
    ) {
        try {
            val messageRequestModel: MessageRequestModel = objectMapper.readValue(message.payload)

            val email = session.attributes["email"] as? String ?: return
            val sender = authRepository.findByEmail(email) ?: return

            if(messageRequestModel.chatId.isNullOrBlank()) {
                val chatId = ChatUtil.generateChatId(sender.id!!, messageRequestModel.receiverId)

                messageRequestModel.copy(chatId=chatId)
            }

            chatMessageService.saveMessage(messageRequestModel)

            val receiverSession = sessions[messageRequestModel.receiverId]
            if (receiverSession != null && receiverSession.isOpen) {
                receiverSession.sendMessage(TextMessage(objectMapper.writeValueAsString(messageRequestModel)))
            }
            // Acknowledge sender
            session.sendMessage(TextMessage(objectMapper.writeValueAsString(messageRequestModel)))
        } catch (e: Exception) {
            e.printStackTrace()
            session.sendMessage(TextMessage("""{"error":"${e.message}"}"""))
        }
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val userIdParam = session.uri?.query?.split("=")?.getOrNull(1)
        userIdParam?.toLongOrNull()?.let {
            connectionManager.removeUser(it)
            println("User $it disconnected")
        }
    }
}