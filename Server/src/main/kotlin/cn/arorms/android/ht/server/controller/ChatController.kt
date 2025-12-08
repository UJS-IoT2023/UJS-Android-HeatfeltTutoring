package cn.arorms.android.ht.server.controller

import cn.arorms.android.ht.server.pojo.dto.ChatMessageDto
import cn.arorms.android.ht.server.pojo.dto.MarkAsReadRequest
import cn.arorms.android.ht.server.pojo.dto.SendMessageRequest
import cn.arorms.android.ht.server.service.ChatMessageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.security.Principal

@RestController
@RequestMapping("/api/chat")
class ChatController @Autowired constructor(
    private val chatMessageService: ChatMessageService,
    private val messagingTemplate: SimpMessagingTemplate
) {

    // REST API endpoints for chat history and management

    // Send a message via REST API
    @PostMapping("/send")
    fun sendMessage(
        @RequestBody request: SendMessageRequest,
        @RequestParam(required = false, defaultValue = "1") senderId: Long
    ): ResponseEntity<ChatMessageDto> {
        try {
            val savedMessage = chatMessageService.sendMessage(senderId, request)
            val messageDto = ChatMessageDto(savedMessage)

            // Send real-time notification to receiver
            messagingTemplate.convertAndSendToUser(
                request.receiverId.toString(),
                "/queue/messages",
                messageDto
            )

            return ResponseEntity.ok(messageDto)
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseEntity.internalServerError().build()
        }
    }

    // Get conversation between two users
    @GetMapping("/conversation/{otherUserId}")
    fun getConversation(
        @PathVariable otherUserId: Long,
        authentication: Authentication
    ): ResponseEntity<List<ChatMessageDto>> {
        try {
            val currentUserId = extractUserIdFromAuthentication(authentication)
            val messages = chatMessageService.getConversation(currentUserId, otherUserId)
            return ResponseEntity.ok(messages)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    // Get all messages for current user
    @GetMapping("/messages")
    fun getUserMessages(authentication: Authentication): ResponseEntity<List<ChatMessageDto>> {
        try {
            val userId = extractUserIdFromAuthentication(authentication)
            val messages = chatMessageService.getUserMessages(userId)
            return ResponseEntity.ok(messages)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    // Get unread messages count
    @GetMapping("/unread/count")
    fun getUnreadMessageCount(authentication: Authentication): ResponseEntity<Map<String, Long>> {
        try {
            val userId = extractUserIdFromAuthentication(authentication)
            val count = chatMessageService.getUnreadMessageCount(userId)
            return ResponseEntity.ok(mapOf("unreadCount" to count))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    // Get unread messages
    @GetMapping("/unread")
    fun getUnreadMessages(authentication: Authentication): ResponseEntity<List<ChatMessageDto>> {
        try {
            val userId = extractUserIdFromAuthentication(authentication)
            val messages = chatMessageService.getUnreadMessages(userId)
            return ResponseEntity.ok(messages)
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    // Mark messages as read
    @PostMapping("/mark-read")
    fun markMessagesAsRead(
        @RequestBody request: MarkAsReadRequest,
        authentication: Authentication
    ): ResponseEntity<Map<String, Int>> {
        try {
            val userId = extractUserIdFromAuthentication(authentication)
            val updatedCount = chatMessageService.markMessagesAsRead(userId, request.messageIds)
            return ResponseEntity.ok(mapOf("updatedCount" to updatedCount))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    // Mark all messages from a specific user as read
    @PostMapping("/mark-all-read/{senderId}")
    fun markAllMessagesAsRead(
        @PathVariable senderId: Long,
        authentication: Authentication
    ): ResponseEntity<Map<String, Int>> {
        try {
            val receiverId = extractUserIdFromAuthentication(authentication)
            val updatedCount = chatMessageService.markAllMessagesAsRead(receiverId, senderId)
            return ResponseEntity.ok(mapOf("updatedCount" to updatedCount))
        } catch (e: Exception) {
            return ResponseEntity.badRequest().build()
        }
    }

    // WebSocket message handling

    // Handle sending messages via WebSocket
    @MessageMapping("/chat.sendMessage")
    fun sendMessage(@Payload chatMessage: ChatMessageDto, principal: Principal) {
        try {
            // Save the message
            val senderId = extractUserIdFromPrincipal(principal)
            val request = SendMessageRequest(
                receiverId = chatMessage.receiverId,
                content = chatMessage.content,
                messageType = chatMessage.messageType
            )

            val savedMessage = chatMessageService.sendMessage(senderId, request)
            val messageDto = ChatMessageDto(savedMessage)

            // Send to receiver
            messagingTemplate.convertAndSendToUser(
                chatMessage.receiverId.toString(),
                "/queue/messages",
                messageDto
            )

            // Send back to sender for confirmation
            messagingTemplate.convertAndSendToUser(
                senderId.toString(),
                "/queue/messages",
                messageDto
            )
        } catch (e: Exception) {
            // Handle error - could send error message back to sender
            messagingTemplate.convertAndSendToUser(
                extractUserIdFromPrincipal(principal).toString(),
                "/queue/errors",
                mapOf("error" to "Failed to send message", "details" to e.message)
            )
        }
    }

    // Handle marking messages as read via WebSocket
    @MessageMapping("/chat.markAsRead")
    fun markAsRead(@Payload messageIds: List<Long>, principal: Principal) {
        try {
            val userId = extractUserIdFromPrincipal(principal)
            val updatedCount = chatMessageService.markMessagesAsRead(userId, messageIds)

            // Send confirmation back to user
            messagingTemplate.convertAndSendToUser(
                userId.toString(),
                "/queue/read-confirm",
                mapOf("updatedCount" to updatedCount, "messageIds" to messageIds)
            )
        } catch (e: Exception) {
            messagingTemplate.convertAndSendToUser(
                extractUserIdFromPrincipal(principal).toString(),
                "/queue/errors",
                mapOf("error" to "Failed to mark messages as read", "details" to e.message)
            )
        }
    }

    // Helper functions to extract user ID from authentication/principal
    // These would need to be implemented based on your JWT authentication setup

    private fun extractUserIdFromAuthentication(authentication: Authentication): Long {
        // TODO: Implement based on your JWT token structure
        // For now, assuming the authentication name contains the user ID
        return authentication.name.toLong()
    }

    private fun extractUserIdFromPrincipal(principal: Principal): Long {

        // For now, assuming the principal name contains the user ID
        return principal.name.toLong()
    }
}
