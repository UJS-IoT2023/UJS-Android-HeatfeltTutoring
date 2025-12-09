package cn.arorms.android.ht.server.service

import cn.arorms.android.ht.server.pojo.dto.ChatDialogueDto
import cn.arorms.android.ht.server.pojo.dto.ChatParticipantDto
import cn.arorms.android.ht.server.pojo.entity.ChatDialogue
import cn.arorms.android.ht.server.pojo.entity.ChatDialogueParticipant
import cn.arorms.android.ht.server.pojo.entity.ChatMessage
import cn.arorms.android.ht.server.pojo.enums.DialogueType
import cn.arorms.android.ht.server.repository.ChatDialogueRepository
import cn.arorms.android.ht.server.repository.ChatDialogueParticipantRepository
import cn.arorms.android.ht.server.repository.ChatMessageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ChatDialogueService(
    private val chatDialogueRepository: ChatDialogueRepository,
    private val chatDialogueParticipantRepository: ChatDialogueParticipantRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val userService: UserService
) {

    // 获取用户的对话列表，包含动态生成的标题
    fun getUserDialogues(userId: Long): List<ChatDialogueDto> {
        val dialogues = chatDialogueRepository.findByParticipants_ParticipantUser_Id(userId)
        return dialogues.map { dialogue ->
            val participants = dialogue.participants.map { participant ->
                ChatParticipantDto(
                    id = participant.participantUser.id!!,
                    username = participant.participantUser.username ?: "Unknown",
                    avatar = null // 可以根据 User 实体的 avatar 字段设置
                )
            }

            val displayTitle = generateDialogueTitle(dialogue, userId)

            ChatDialogueDto(
                id = dialogue.id!!,
                title = displayTitle,
                dialogueType = dialogue.dialogueType,
                lastMessageContent = dialogue.lastMessageContent,
                updatedAt = dialogue.updatedAt,
                participants = participants
            )
        }.sortedByDescending { it.updatedAt }
    }

    // 动态生成对话标题
    private fun generateDialogueTitle(dialogue: ChatDialogue, currentUserId: Long): String {
        return when (dialogue.dialogueType) {
            DialogueType.PRIVATE -> {
                // 私聊：显示对方的用户名
                val otherParticipant = dialogue.participants.firstOrNull { it.participantUser.id != currentUserId }
                otherParticipant?.participantUser?.username ?: "Unknown User"
            }
            DialogueType.GROUP -> {
                // 群聊：如果有自定义标题则使用，否则动态生成
                dialogue.title ?: run {
                    val otherParticipants = dialogue.participants
                        .filter { it.participantUser.id != currentUserId }
                        .take(3)
                    val names = otherParticipants.map { it.participantUser.username ?: "Unknown" }
                    names.joinToString(", ") + if (dialogue.participants.size > 4) "..." else ""
                }
            }
        }
    }

    // 创建新对话
    @Transactional
    fun createDialogue(creatorId: Long, participantIds: List<Long>, title: String? = null): ChatDialogue {
        val dialogueType = if (participantIds.size == 1) DialogueType.PRIVATE else DialogueType.GROUP

        val dialogue = ChatDialogue(
            dialogueType = dialogueType,
            title = if (dialogueType == DialogueType.GROUP) title else null
        )
        val savedDialogue = chatDialogueRepository.save(dialogue)

        // 添加参与者
        val allParticipantIds = participantIds + creatorId
        allParticipantIds.forEach { userId ->
            val participant = ChatDialogueParticipant(
                dialogue = savedDialogue,
                participantUser = userService.getReferenceById(userId) // 简化的 User 对象
            )
            chatDialogueParticipantRepository.save(participant)
        }

        return savedDialogue
    }

    // 发送消息
    @Transactional
    fun sendMessage(dialogueId: Long, senderId: Long, content: String): ChatMessage {
        val dialogue = chatDialogueRepository.findById(dialogueId).orElseThrow {
            IllegalArgumentException("Dialogue not found")
        }

        val message = ChatMessage(
            dialogue = dialogue,
            sender = userService.getReferenceById(senderId), // 简化的 User 对象
            content = content
        )
        val savedMessage = chatMessageRepository.save(message)

        // 更新对话的最后消息和更新时间
        dialogue.lastMessageContent = content
        dialogue.updatedAt = LocalDateTime.now()
        chatDialogueRepository.save(dialogue)

        return savedMessage
    }

    // 获取对话的消息历史
    fun getDialogueMessages(dialogueId: Long): List<ChatMessage> {
        return chatMessageRepository.findByDialogue_IdOrderByCreatedAt(dialogueId)
    }
}
