package cn.arorms.android.ht.server.pojo.dto

import java.time.LocalDateTime

data class ChatMessageDto(
    val id: Long? = null,
    val dialogueId: Long,
    val senderId: Long,
    val senderUsername: String,
    val content: String,
    val createdAt: LocalDateTime = LocalDateTime.now()
)
