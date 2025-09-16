package com.medics.zmed.config

import com.medics.zmed.component.JwtUtil
import com.medics.zmed.controller.chat_handler.ChatWebSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry


@Configuration
@EnableWebSocket
class WebSocketConfig(
    private val chatHandler: ChatWebSocketHandler,
   // private val jwtHandshakeInterceptor: JwtHandshakeInterceptor
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry.addHandler(chatHandler, "/ws/chat")
         //   .addInterceptors(jwtHandshakeInterceptor) // JWT validation before handshake
            .setAllowedOrigins("*") // allow all origins
    }
}
