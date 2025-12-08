package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.dto.ChatMessageDto
import cn.arorms.android.ht.server.pojo.dto.SendMessageRequest
import cn.arorms.android.ht.server.pojo.entity.ChatMessage
import cn.arorms.android.ht.server.repository.ChatMessageRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class ChatMessageService @Autowired constructor(
    private val chatMessageRepository: ChatMessageRepository,
    private val userService: UserService
) {

    // Send a new message
    fun sendMessage(senderId: Long, request: SendMessageRequest): ChatMessage {
        // Validate users exist
        userService.getUserById(senderId)
            .orElseThrow { RuntimeException("Sender not found with id: $senderId") }
        userService.getUserById(request.receiverId)
            .orElseThrow { RuntimeException("Receiver not found with id: ${request.receiverId}") }

        val chatMessage = ChatMessage(
            senderId = senderId,
            receiverId = request.receiverId,
            content = request.content,
            messageType = request.messageType,
            isRead = false,
            createdAt = LocalDateTime.now()
        )

        return chatMessageRepository.save(chatMessage)
    }

    // Get conversation between two users
    fun getConversation(userId1: Long, userId2: Long): List<ChatMessageDto> {
        val messages = chatMessageRepository.findConversationBetweenUsers(userId1, userId2)
        return messages.map { ChatMessageDto(it) }
    }

    // Get all messages for a user (sent and received)
    fun getUserMessages(userId: Long): List<ChatMessageDto> {
        val sentMessages = chatMessageRepository.findBySenderId(userId)
        val receivedMessages = chatMessageRepository.findByReceiverId(userId)
        val allMessages = (sentMessages + receivedMessages).sortedBy { it.createdAt }
        return allMessages.map { ChatMessageDto(it) }
    }

    // Get unread messages for a user
    fun getUnreadMessages(userId: Long): List<ChatMessageDto> {
        val unreadMessages = chatMessageRepository.findByReceiverIdAndIsRead(userId, false)
        return unreadMessages.map { ChatMessageDto(it) }
    }

    // Mark messages as read
    fun markMessagesAsRead(userId: Long, messageIds: List<Long>): Int {
        val messages = chatMessageRepository.findAllById(messageIds)
        var updatedCount = 0

        for (message in messages) {
            // Only allow marking messages as read if the user is the receiver
            if (message.receiverId == userId && !message.isRead) {
                message.markAsRead()
                chatMessageRepository.save(message)
                updatedCount++
            }
        }

        return updatedCount
    }

    // Mark all messages from a specific sender as read for the receiver
    fun markAllMessagesAsRead(receiverId: Long, senderId: Long): Int {
        val messages = chatMessageRepository.findConversationBetweenUsers(receiverId, senderId)
        var updatedCount = 0

        for (message in messages) {
            if (message.receiverId == receiverId && !message.isRead) {
                message.markAsRead()
                chatMessageRepository.save(message)
                updatedCount++
            }
        }

        return updatedCount
    }

    // Get unread message count for a user
    fun getUnreadMessageCount(userId: Long): Long {
        return chatMessageRepository.countByReceiverIdAndIsRead(userId, false)
    }

    // Delete a message (only by sender or receiver)
    fun deleteMessage(userId: Long, messageId: Long): Boolean {
        val message = chatMessageRepository.findById(messageId)
            .orElseThrow { RuntimeException("Message not found with id: $messageId") }

        // Only sender or receiver can delete the message
        if (message.senderId == userId || message.receiverId == userId) {
            chatMessageRepository.delete(message)
            return true
        }

        return false
    }

    // Get message by ID
    fun getMessageById(messageId: Long): ChatMessageDto? {
        val message = chatMessageRepository.findById(messageId)
        return if (message.isPresent) ChatMessageDto(message.get()) else null
    }
}
