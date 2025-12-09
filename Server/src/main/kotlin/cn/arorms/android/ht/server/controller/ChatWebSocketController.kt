package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.ChatMessageDto
import cn.arorms.android.ht.server.service.ChatDialogueService
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.stereotype.Controller

@Controller
class ChatWebSocketController(
    private val chatDialogueService: ChatDialogueService
) {

    // 处理发送消息
    @MessageMapping("/chat/{dialogueId}/send")
    @SendTo("/topic/dialogue/{dialogueId}")
    fun sendMessage(
        @DestinationVariable dialogueId: Long,
        @Payload messageDto: ChatMessageDto,
        headerAccessor: SimpMessageHeaderAccessor
    ): ChatMessageDto {
        // 保存消息到数据库
        val savedMessage = chatDialogueService.sendMessage(
            dialogueId = dialogueId,
            senderId = messageDto.senderId,
            content = messageDto.content
        )

        // 返回包含完整信息的消息 DTO
        return ChatMessageDto(
            id = savedMessage.id,
            dialogueId = dialogueId,
            senderId = savedMessage.sender.id!!,
            senderUsername = savedMessage.sender.username ?: "Unknown",
            content = savedMessage.content,
            createdAt = savedMessage.createdAt
        )
    }

    // 处理用户加入对话（可选，用于状态管理）
    @MessageMapping("/chat/{dialogueId}/join")
    @SendTo("/topic/dialogue/{dialogueId}")
    fun joinDialogue(
        @DestinationVariable dialogueId: Long,
        @Payload joinMessage: Map<String, Any>
    ): Map<String, Any> {
        val userId = joinMessage["userId"] as Long
        val username = joinMessage["username"] as String

        return mapOf(
            "type" to "JOIN",
            "userId" to userId,
            "username" to username,
            "message" to "$username joined the dialogue"
        )
    }
}
