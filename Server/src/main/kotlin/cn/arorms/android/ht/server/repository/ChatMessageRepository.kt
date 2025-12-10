package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.ChatMessage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : JpaRepository<ChatMessage, Long> {
    // 根据对话ID查找所有消息，并按时间排序
    fun findByDialogue_IdOrderByCreatedAt(dialogueId: Long): List<ChatMessage>

    // 根据对话ID查找最新消息（分页支持）
    fun findByDialogue_IdOrderByCreatedAtDesc(dialogueId: Long, pageable: org.springframework.data.domain.Pageable): org.springframework.data.domain.Page<ChatMessage>
}
