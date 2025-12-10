package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.ChatDialogueParticipant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatDialogueParticipantRepository : JpaRepository<ChatDialogueParticipant, Long> {
    // 您可以在这里添加自定义的查询方法，例如根据对话ID查找参与者
    fun findByDialogueId(dialogueId: Long): List<ChatDialogueParticipant>
    fun findByParticipantUserId(userId: Long): List<ChatDialogueParticipant>
}
