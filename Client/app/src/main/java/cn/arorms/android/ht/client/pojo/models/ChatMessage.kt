package cn.arorms.android.ht.client.pojo.models

import java.time.LocalDateTime

data class Participant(
    val id: Long,
    val username: String? = null,
    val avatar: String? = null
)

data class Dialogue(
    var id: Long? = null,
    val title: String? = null,
    val dialogueType: String? = null,
    val lastMessageContent: String? = null,
    val updatedAt: LocalDateTime? = null,
    val participants: List<Participant>? = null
)

data class ChatMessage(
    var id: Long? = null,
    val dialogueId: Long? = null,
    val senderId: Long,
    val senderUsername: String? = null,
    val content: String,
//    val messageType: String = "text",
    val createdAt: LocalDateTime? = null
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
