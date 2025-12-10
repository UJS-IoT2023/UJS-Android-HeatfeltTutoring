package cn.arorms.android.ht.server.pojo.dto

import cn.arorms.android.ht.server.pojo.enums.DialogueType
import java.time.LocalDateTime

data class ChatDialogueDto(
    val id: Long,
    val title: String, // 动态生成的标题
    val dialogueType: DialogueType,
    val lastMessageContent: String?,
    val updatedAt: LocalDateTime,
    val participants: List<ChatParticipantDto> // 参与者列表
)

data class ChatParticipantDto(
    val id: Long,
    val username: String,
    val avatar: String?
)
