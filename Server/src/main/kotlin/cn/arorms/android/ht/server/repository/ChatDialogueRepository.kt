package cn.arorms.android.ht.server.repository

import cn.arorms.android.ht.server.pojo.entity.ChatDialogue
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatDialogueRepository : JpaRepository<ChatDialogue, Long> {
    // 您可以在这里添加自定义的查询方法
    // 根据用户ID查找其参与的所有对话
    fun findByParticipants_participantUser_id(userId: Long): List<ChatDialogue>
}
