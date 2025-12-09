package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Dialogue(
    var id: Long? = null,
    val participantIds: List<Long>,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val lastMessageAt: LocalDateTime? = null,
    val lastMessage: String? = null
)

data class ChatMessage(
    var id: Long? = null,
    val dialogueId: Long,
    val senderId: Long,
    val content: String,
    val messageType: String = "text",
    val isRead: Boolean = false,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class SendMessageRequest(
    val content: String,
    val messageType: String = "text"
)

data class CreateDialogueRequest(
    val participantIds: List<Long>
)

data class MarkAsReadRequest(
    val messageIds: List<Long>
)
