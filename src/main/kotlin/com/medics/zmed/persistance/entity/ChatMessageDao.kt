package com.medics.zmed.persistance.entity
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "message")
data class ChatMessageDao(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val chatId: String,   // deterministic chatId
    val senderId: Long,
    val receiverId: Long,
    val content: String,

    @Enumerated(EnumType.STRING)
    val status: MessageStatus = MessageStatus.SENT,

    val createdAt: LocalDateTime = LocalDateTime.now()
)

enum class MessageStatus {
    SENT, DELIVERED, SEEN
}

