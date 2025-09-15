package com.medics.zmed.component

import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Component
class ConnectionManager {
    private val userSessions = ConcurrentHashMap<Long, WebSocketSession>()

    fun addUser(userId: Long, session: WebSocketSession) {
        userSessions[userId] = session
    }

    fun removeUser(userId: Long) {
        userSessions.remove(userId)
    }

    fun getSession(userId: Long): WebSocketSession? = userSessions[userId]
}
