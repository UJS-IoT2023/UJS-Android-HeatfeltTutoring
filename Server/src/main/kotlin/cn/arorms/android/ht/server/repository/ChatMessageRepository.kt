package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {

    // Find messages between two users (bidirectional)
    @Query("SELECT m FROM ChatMessage m WHERE (m.senderId = :userId1 AND m.receiverId = :userId2) OR (m.senderId = :userId2 AND m.receiverId = :userId1) ORDER BY m.createdAt ASC")
    fun findConversationBetweenUsers(@Param("userId1") userId1: Long, @Param("userId2") userId2: Long): List<ChatMessage>

    // Find unread messages for a user
    fun findByReceiverIdAndIsRead(receiverId: Long, isRead: Boolean): List<ChatMessage>

    // Find messages sent by a user
    fun findBySenderId(senderId: Long): List<ChatMessage>

    // Find messages received by a user
    fun findByReceiverId(receiverId: Long): List<ChatMessage>

    // Count unread messages for a user
    fun countByReceiverIdAndIsRead(receiverId: Long, isRead: Boolean): Long
}
