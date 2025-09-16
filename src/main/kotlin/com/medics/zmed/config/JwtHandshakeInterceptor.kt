package com.medics.zmed.config

import com.medics.zmed.component.JwtUtil
import com.medics.zmed.domain.repository.AuthRepository
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

@Component
class JwtHandshakeInterceptor(
    private val jwtUtil: JwtUtil,
    private val authRepository: AuthRepository
) : HandshakeInterceptor {
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val authHeader = request.headers.getFirst("Authorization") ?: return false
        if (!authHeader.startsWith("Bearer ")) return false

        val token = authHeader.removePrefix("Bearer ").trim()
        if (!jwtUtil.isTokenValid(token, isRefreshToken = false)) return false

        val email = jwtUtil.getEmailFromToken(token)
        val user = authRepository.findByEmail(email) ?: return false

        attributes["userId"] = user.id!!
        attributes["email"] = email
        return true
    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {}
}
