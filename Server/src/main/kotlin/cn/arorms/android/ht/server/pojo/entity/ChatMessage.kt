package cn.arorms.android.ht.server.pojo.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "chat_messages")
data class ChatMessage(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "sender_id", nullable = false)
    var senderId: Long,

    @Column(name = "receiver_id", nullable = false)
    var receiverId: Long,

    @Column(nullable = false)
    var content: String,

    @Column(name = "message_type", nullable = false)
    var messageType: String = "text", // text, image, etc.

    @Column(name = "is_read", nullable = false)
    var isRead: Boolean = false,

    @Column(name = "created_at", nullable = false)
    var createdAt: LocalDateTime = LocalDateTime.now()
) {
    // Helper method to mark as read
    fun markAsRead() {
        this.isRead = true
    }
}
