package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.entity.ChatMessage
import java.time.LocalDateTime

data class ChatMessageDto(
    var id: Long? = null,
    val senderId: Long,
    val receiverId: Long,
    val content: String,
    val messageType: String = "text",
    val isRead: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    constructor(chatMessage: ChatMessage) : this(
        id = chatMessage.id,
        senderId = chatMessage.senderId,
        receiverId = chatMessage.receiverId,
        content = chatMessage.content,
        messageType = chatMessage.messageType,
        isRead = chatMessage.isRead,
        createdAt = chatMessage.createdAt
    )
}

// DTO for sending messages
data class SendMessageRequest(
    val receiverId: Long,
    val content: String,
    val messageType: String = "text"
)

// DTO for marking messages as read
data class MarkAsReadRequest(
    val messageIds: List<Long>
)
